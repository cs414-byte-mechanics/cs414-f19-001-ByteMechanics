package webconnection;

import database.*;
import Game.*;
import org.java_websocket.WebSocket;
import java.util.ArrayList;

public class UpdateFactory
{
    private DatabaseHandler db;

    public UpdateFactory() {
        db = new DatabaseHandler();
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
            default:
                System.err.println("Invalid action communication type.");
                return new Update();
        }
    }

    private Update buildUpdateBoard(Action action) {
        try {
            Game game = new Game();
            game.loadExistingGame(action);
        
            if (action.desiredMoves != null) {
                ArrayList<Integer> movesRow = new ArrayList<>();
                ArrayList<Integer> movesCol = new ArrayList<>();
                for (int i = 1; i < action.desiredMoves.length; i++){
                    int col = action.desiredMoves[i] % 10;
                    int row = (action.desiredMoves[i] - col)/10;
                    movesCol.add(col);
                    movesRow.add(row);
                }

                boolean moveSucceeded = game.performMove(action.desiredMoves[0], movesRow, movesCol);
            
                if(moveSucceeded){
                    game.saveMatchState(Integer.parseInt(action.matchID));
                }
            }

            Update update = new Update();
            update.communicationType = "updateBoard";
            update.matchID = action.matchID;
            update.playerName = action.playerName;
//             update.pieceID =  "M";
            update.updatedBoard = game.getBoard();
            update.whoseTurn = "opponent";

            return update;
        } catch (Exception e){
            System.err.println("Game cannot be fetched");
            return null;
        }
    

    }

    private Update registerUser(Action action) {
        try {
            db.registerUser(action);
            Update update = new Update();
            update.communicationType = "registrationSuccess";
            update.userEmail = action.userEmail;
            update.userName = action.userName;
            update.successMessage = "User account has been successfully created.";

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
        update.successMessage = "User has successfully logged out.";
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

}
