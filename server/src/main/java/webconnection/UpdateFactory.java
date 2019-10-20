package webconnection;
import database.*;
import Game.*;
import org.java_websocket.WebSocket;
import java.util.ArrayList;

public class UpdateFactory {

    private DatabaseHandler db;

    //client that action for the corresponding update was sent from
    private WebSocket sentFrom;

    //clients that the update will be sent to
    private ArrayList<WebSocket> sendTo;

    public UpdateFactory(){
        db = new DatabaseHandler();
    }
    
    public Update performAction(Action action){
        //decide which update to construct given the type of action sent from the client

        switch(action.communicationType){
            case "requestMoves":
                return performMove(action); 
            case "registerUser": 
                return registerUser(action);
            case "requestBeginNewMatch": 
                return beginNewMatch(action);
            case "invitation": 
                return sendInvite(action);
            case "invitationResponse": 
                return null; 
            case "quitMatch": 
                return endMatch(action);
            case "unregisterUser": 
                return null;
            case "attemptLogin": 
                return loginUser(action); 
            default: 
                System.err.println("Invalid action communication type.");
                return null;
        }
    }

    private Update performMove(Action action) {
        //Perform move here
        
        Update update = new Update();
        
        update.communicationType = "updateBoard";
        update.matchID = "dummy_match_ID";
        update.playerName = "dummy_player_name";
        update.pieceID =  4;
        update.updatedBoard = new int[3][3];
        update.updatedBoard[0][0] = 1;
        update.updatedBoard[0][1] = 2;
        update.whoseTurn = "opponent";
        return update;
    }

    private Update registerUser(Action action) {
        try {
            if(db.performDBSearch(action)) {
                Update update = new Update();
                update.communicationType = "registrationSuccess";
                update.userEmail = action.userEmail;
                update.userName = action.userName;
                update.successMessage = "User account has been successfully created.";
                return update;
            } else {
                //return null for now
                return null;
            }

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public Update beginNewMatch(Action action) {
        Update update = new Update();
        update.communicationType = "beginNewMatch";
        update.matchBeginTime = "dummy_match_begin_time";       //do we need this?
      
      
        //Creates a new Game object which creates board, and initializes it, and players
        Game game = new Game();
        update.initialBoard = game.getGameBoard();
        update.whoseTurn = "opponent";               //will be determined by game, how?

      
        //Saves this to database TBD
        update.matchID = "dummy_math_ID";            
        return update;
    }

    private Update sendInvite(Action action) {
        Update update = new Update();
        
        update.communicationType = "invitation";
        update.invitationFrom = "player1";
        update.invitationTo = "player2";
        update.invitationTime = "dummy_time";
        return update;
    }

    private Update endMatch(Action action) {
        Update update = new Update();
        
        update.communicationType = "endMatch";
        update.matchID = "dummy_match_ID";
        update.endCondition = "quit";
        update.winnerName = "player1";
        update.loserName = "player2";
        update.matchEndTime = "dummy_end_time";
        return update;
    }

    private Update loginUser(Action action) {
        Update update = new Update();
        
        update.communicationType = "loginSuccess";
        update.invitations = null;
        update.matchesInProgress = null;
        update.matchesCompleted = null;
        return update;
    }
}
