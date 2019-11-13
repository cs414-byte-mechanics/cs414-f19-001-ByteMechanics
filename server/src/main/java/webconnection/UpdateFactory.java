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

    private String updateTurn(Update update, Action action){
        if (update.communicationType == "updateBoard")
            if (update.whoseTurn == action.playerOneName)
                update.whoseTurn = action.playerTwoName;

            if (update.whoseTurn == action.playerTwoName)
                update.whoseTurn = action.playerOneName;

        return update.whoseTurn;
    }

    private Update buildUpdateBoard(Action action) {
        try {
            Game game = new Game();
//            game.loadExistingGame(action);
            Update update = new Update();
            game.processMove(action.desiredMoves, congoGame);

            update.communicationType = "updateBoard";
            update.matchID = action.matchID ;
            update.playerName = action.playerName ;
            update.pieceID =  action.pieceID ;
            update.updatedBoard = congoGame.getBoardForDatabase();
            update.whoseTurn = updateTurn(update, action);
            update.statusMessage = "The player's move was valid and the board has been updated";//constructMessage(communicationType, update) ;

            return update;

        } catch (Exception e){
            ServerError error = new ServerError(102, e.getMessage());
//            Update update = new Update();
//            update.communicationType = "ErrorInvalidMove";
//            update.message = "GameBoard not found! Unable to make move";
//
//            System.err.println("Game cannot be fetched");
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
