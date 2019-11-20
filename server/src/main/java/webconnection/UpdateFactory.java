package webconnection;
import database.*;
import Game.*;

import java.util.ArrayList;

public class UpdateFactory
{
    private DatabaseHandler db;
    private GameBoard congoGame;
    private GameBoard gameBoard;
    private Game game;
    private boolean moveSucceeded ;

    public UpdateFactory() {

        db = new DatabaseHandler();
        congoGame = new GameBoard();
        congoGame.initialize();
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

    private String updateTurn(Update update, Action action) {
        if (update.communicationType == "updateBoard")
            if (update.whoseTurn == action.playerOneName)
                update.whoseTurn = action.playerTwoName;

            if (update.whoseTurn == action.playerTwoName)
                update.whoseTurn = action.playerOneName;

        return update.whoseTurn;
    }

    private String findWinner(String communicationType, Update update, int currLocation) throws Exception {
        String winner = null;
        int activePlayer;

        if (communicationType == "endMatch") {
//            activePlayer = congoGame.findActivePlayer(update.updatedBoard, currLocation);
            GamePiece piece = congoGame.getGamePiece(currLocation/10, currLocation%10);
            activePlayer = piece.player;

            if (activePlayer == 1)
                winner= "playerOne";

            if (activePlayer == 2)
                winner= "playerTwo";
        }
        return winner;
    }

    private Update buildUpdateBoard(Action action) {
        try {
            Game game = new Game();
            Update update = new Update();
            /** At this point we track if opponent's lion is in castle, and in this case we can still play and perform move and keep playing, otherwise lion is captured and keep playing does not make sense!!!!*/
            boolean lionExist = congoGame.lionInCastle(congoGame.getBoardForDatabase(), action.desiredMoves[0]);
            if (lionExist) game.processMove(action.desiredMoves, congoGame);
            else throw new Exception("Opponent's lion does not exist");
            /** after performing valid move, we need to check if lion is till in castle or it is captured?*/
            lionExist = congoGame.lionInCastle(congoGame.getBoardForDatabase(), action.desiredMoves[0]);

            update.communicationType = lionExist ? "updateBoard" : "endMatch";
            update.statusMessage = lionExist ? "The player's move was valid and the board has been updated" : "Lion is captured, Game is Over!";
            update.matchID = action.matchID;
            update.playerName = action.playerName ;
            update.pieceID =  action.pieceID ;
            update.updatedBoard = congoGame.getBoardForDatabase();
            update.whoseTurn = updateTurn(update, action);

            update.winnerName = action.playerName = findWinner(update.communicationType, update, action.desiredMoves[0]); /*this might need to be replace with action.playerName later*/

            return update;

        } catch (Exception e){
            e.printStackTrace();
            ServerError error = new ServerError(102, e.getMessage());
            return error;
        }
    }

    private Update registerUser(Action action) {
        try {
            db.registerUser(action);
            Update update = new Update();
            update.communicationType = "registrationSuccess";
            update.userEmail = action.userEmail;
            update.userName = action.userName;
            update.statusMessage = "User account has been successfully created.";

            return update;

        } catch (Exception e){
            System.out.println(e);
            ServerError error = new ServerError(101, e.getMessage());
            return error;
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
            ServerError error = new ServerError(100, e.getMessage());
            return error;
        }
    }

    private Update buildLogoutSuccess(Action action) {
        Update update = new Update();
        update.communicationType = "logoutSuccess";
        update.statusMessage = "User has successfully logged out.";
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
            return new ServerError(-1, e.getMessage());
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
        try {
            update.searchResults = db.searchUser(action);
        } catch(Exception e) {}

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
