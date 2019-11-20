package webconnection;
import database.*;
import Game.*;

import java.util.ArrayList;

public class UpdateFactory
{
    private DatabaseHandler db;
    //private GameBoard congoGame;
    //private String communicationType;
    private GameBoard gameBoard;
    private Game game;

    public UpdateFactory() {

        db = new DatabaseHandler();
       //congoGame = new GameBoard();
        // congoGame.initialize();
        //temporarily starting a game in the server until the client is ready to handle it
        game = new Game();
        gameBoard = game.getGameBoard();
        gameBoard.initialize();
    }

    public Update getUpdate(Action action) {
        //decide which update to construct given the type of action sent from the client
        switch(action.communicationType)
        {
            case "requestMoves": return this.buildUpdateBoard(action);
            case "registerUser": return this.registerUser(action);
            case "requestBeginNewMatch": return this.createNewMatch(action);
            case "invitation": return this.buildInvitation();
            case "invitationResponse": return null;
            case "quitMatch": return this.buildEndMatch();
            case "unregisterUser": return null;
            case "attemptLogin": return this.logIn(action);
            case "attemptLogout": return this.buildLogoutSuccess(action);
            case "searchUser": return this.buildSearchResult(action);
            case "sendInvitation": return this.buildInvitationSentStatus(action);
            default:
                System.err.println("Invalid action communication type.");
                return new Update();
        }
    }

    /* helper routine to fill out message field with proper message*/
    private String constructMessage(String communicationType, Update update) {
        switch (communicationType){
            case "updateBoard": return  "The player's move was valid and the board has been updated";
            case "errorInvalidMove": update.errorCode= 102; return ServerError.getErrorMessage(update.errorCode);
            case "endMatch": return "Lion is captured, Game is Over!";

            default:
                System.err.println("Message has not been constructed!!");
                return null;
        }
    }

    private void updateTurn(Update update, Action action){
        String nextPlayer;
        if (update.communicationType.compareTo("updateBoard") == 0) {
            /* swap which player is taking a turn next */
            nextPlayer = (action.playerName.compareTo(action.playerOneName) == 0) ? action.playerTwoName : action.playerOneName;
        }
        else  /* move did not succeed or game ended so it is the same player's turn again.*/
            nextPlayer = action.playerName;

        update.whoseTurn = nextPlayer;
        update.playerName = nextPlayer;
    }

    private String findWinner(String communicationType, Update update, int currLocation) {
        String winner = null;
        int activePlayer;

        if (communicationType == "endMatch") {
//            activePlayer = congoGame.findActivePlayer(update.updatedBoard, currLocation);
            GamePiece piece = gameBoard.getGamePiece(currLocation/10, currLocation%10);
            activePlayer = piece.player;

            if (activePlayer == 1)
                winner= "playerOne";

            if (activePlayer == 2)
                winner= "playerTwo";
        }
        return winner;
    }

    private Update wrapUpResponse(Update update, Action action, String communicationType)
    {
        System.out.println(action.toString());
        update.communicationType = communicationType;
        update.communicationVersion = 0;
        update.matchID = action.matchID ;
        //update.playerName = action.playerName ;
        update.pieceID =  action.pieceID ;

        /* fill out board, turn and message filed */
        update.updatedBoard = gameBoard.getBoardForDatabase();
        updateTurn(update, action);
        System.out.println("Whose turn next- "+update.whoseTurn);
        update.message = constructMessage(communicationType, update);

        /* find who is winner*/
        update.winnerName = action.playerName = findWinner(communicationType, update, action.desiredMoves[0]); /*this might need to be replace with action.playerName later*/

        return update;
    }

    private Update buildUpdateBoard(Action action) {
        boolean moveSucceeded ;
        boolean sequenceCorrect;
        String communicationType = "";

        try {
            //Game game = new Game();
//            game.loadExistingGame(action);
            Update update = new Update();

            System.out.println("Action "+action.toString());
            System.out.println(gameBoard.toString());
//            sequenceCorrect = game.moveSequenceCorrect(action, gameBoard, action.desiredMoves[0]);
            /** At this point we track if opponent's lion is in castle, and in this case we can still play and perform move and keep playing, otherwise lion is captured and keep playing does not make sense!!!!*/
//            boolean lionExist = congoGame.lionInCastle(congoGame.getBoardForDatabase(), action.desiredMoves[0]);
//            if (lionExist)
//                moveSucceeded = game.processMove(action.desiredMoves, gameBoard);
//            else //need to alert that opponent's lion does not exist !!!!!

            //if (moveSucceeded == true) {
            //if (sequenceCorrect && moveSucceeded) { /* if move is valid/legal */
            if (game.moveSequenceCorrect(action, gameBoard, action.desiredMoves[0]) &&
                    game.processMove(action.desiredMoves, gameBoard))       {
                /** after performing valid move, we need to check if lion is till in castle or it is captured?*/
                boolean lionExist = gameBoard.lionInCastle(gameBoard.getBoardForDatabase(), action.desiredMoves[0]);
                if (lionExist)  /*lion is not captured, so we need to return updated board back to client */
                    communicationType = "updateBoard";

                if (!lionExist)  /* move is valid/legal, but lion is captured, so we need to terminate game (GAME OVER)*/
                    communicationType = "endMatch";
            }
            else /* move isn't valid, so we return error message */
                communicationType = "errorInvalidMove";

            wrapUpResponse(update, action, communicationType);
            //game.saveMatchState(Integer.parseInt(action.matchID), update.whoseTurn);
            return update;

        } catch (Exception e){
            Update update = new Update();
            update.communicationType = "ErrorInvalidMove";
            update.message = "GameBoard not found! Unable to make move";

            System.err.println("Game cannot be fetched");
            return update;
        }
    }

    private Update registerUser(Action action) {
        try {
            db.registerUser(action);
            Update update = new Update();
            update.communicationType = "registrationSuccess";
            update.userEmail = action.userEmail;
            update.userName = action.userName;
            update.message = "User account has been successfully created.";

            return update;

        } catch (Exception e){
            System.out.println(e);
            //Return error update
            return null;
        }
    }

    private Update logIn(Action action) {
        try {
            Update update = new Update();
            update.userName = db.attemptLogin(action);
            update.userEmail = action.userEmail;
            update.communicationType = "loginSuccess";

            return update;

        } catch (Exception e){
            System.out.println(e);
            //Return error update "errorInvalidRegistration"
            return null;
        }
    }

    private Update buildLogoutSuccess(Action action) {
        Update update = new Update();
        update.communicationType = "logoutSuccess";
        update.message = "User has successfully logged out.";
        return update;
    }

    private Update createNewMatch(Action action) {
        try {
            Game game = new Game();
            Update update = new Update();
            
            update.communicationType = "beginNewMatch";
            update.matchID = Integer.toString(game.createNewGame(action));
            update.initialBoard = game.getBoard();
            update.whoseTurn = action.playerOneName;
    //         update.matchBeginTime = "dummy_match_begin_time";
            return update;
        } catch(Exception e){
            System.err.println("New match cannot be created");
            return null;
        }

    }

    private Update buildInvitation() {
        Update update = new Update();
        update.communicationType = "invitation";
        update.invitationFrom = "player1";
        update.invitationTo = "player2";
        update.invitationTime = "dummy_time";
        return update;
    }

    private Update buildEndMatch() {
        Update update = new Update();
        update.communicationType = "endMatch";
        update.matchID = "dummy_match_ID";
        update.endCondition = "quit";
        update.winnerName = "player1";
        update.loserName = "player2";
        update.matchEndTime = "dummy_end_time";
        return update;
    }

    private Update buildSearchResult(Action action) {
        Update update = new Update();
        update.communicationType = "searchResult";
        update.userName= action.userName;
        String databaseSearchResult = "";

        try {
            databaseSearchResult = db.searchUser(action);
        } catch(Exception e) {}

        if (databaseSearchResult.equals("user not found")) {
            update.userFound = false;
        }
        else {
            update.userFound = true;
        }

        return update;
    }
  
    private Update buildInvitationSentStatus(Action action) {
        Update update = new Update();
        update.communicationType = "invitationSentStatus";
        try {
            db.sendGameInvitation(action);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            update.invitationSent = false;
            update.statusMessage = e.toString();
            return update;
        }
        update.invitationSent = true;
        return update;
    }
}
