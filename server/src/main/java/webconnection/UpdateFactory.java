package webconnection;
import database.*;
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
            case "requestMoves": return this.buildUpdateBoard();
            case "registerUser": return this.registerUser(action);
            case "requestBeginNewMatch": return this.buildBeginNewMatch();
            case "invitation": return this.buildInvitation();
            case "invitationResponse": return null;
            case "quitMatch": return this.buildEndMatch();
            case "unregisterUser": return null;
            case "attemptLogin": return this.logIn(action);
            case "attemptLogout": return this.buildLogoutSuccess(action);
            default:
                System.err.println("Invalid action communication type.");
                return new Update();
        }
    }

    private Update buildUpdateBoard() {
        Update update = new Update();
        update.communicationType = "updateBoard";
        update.matchID = "dummy_match_ID";
        update.playerName = "dummy_player_name";
        update.pieceID =  4;
        //  update.pieceID =  action.pieceID;
        update.updatedBoard = new int[3][3];
        update.updatedBoard[0][0] = 1;
        update.updatedBoard[0][1] = 2;
        update.whoseTurn = "opponent";

        return update;
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

    private Update buildBeginNewMatch() {
        Update update = new Update();
        update.communicationType = "beginNewMatch";
        update.matchID = "dummy_math_ID";
        update.initialBoard = new int[5][5];
        update.initialBoard[0][0] = 1;
        update.initialBoard[0][1] = 2;
        update.whoseTurn = "opponent";
        update.matchBeginTime = "dummy_match_begin_time";
        return update;
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

}
