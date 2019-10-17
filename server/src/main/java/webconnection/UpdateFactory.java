package webconnection;
import database.*;
import org.java_websocket.WebSocket;
import java.util.ArrayList;

public class UpdateFactory
{

    private Action action;
    private Update update;
    private DatabaseHandler db;

    //client that action for the corresponding update was sent from
    private WebSocket sentFrom;

    //clients that the update will be sent to
    private ArrayList<WebSocket> sendTo;

    public UpdateFactory(Action action, WebSocket sentFrom)
    {
        db = new DatabaseHandler();

        this.action = action;
        this.sentFrom = sentFrom;
        this.sendTo = new ArrayList<>();
        this.update = new Update();

        //decide which update to construct given the type of action sent from the client
        switch(action.communicationType)
        {
            case "requestMoves": this.buildUpdateBoard(); break;
            case "registerUser": this.buildRegistrationSuccess(); break;
            case "requestBeginNewMatch": this.buildBeginNewMatch(); break;
            case "invitation": this.buildInvitation(); break;
            case "invitationResponse": this.update = null; break;
            case "quitMatch": this.buildEndMatch(); break;
            case "unregisterUser": this.update = null; break;
            case "attemptLogin": this.buildLoginSuccess(); break;
            default: System.err.println("Invalid action communication type.");
        }
    }

    private void buildUpdateBoard()
    {
        this.update.communicationType = "updateBoard";
        this.update.matchID = "dummy_match_ID";
        this.update.playerName = "dummy_player_name";
        this.update.pieceID =  4;
        this.update.updatedBoard = new int[3][3];
        this.update.updatedBoard[0][0] = 1;
        this.update.updatedBoard[0][1] = 2;
        this.update.whoseTurn = "opponent";
        this.sendTo.add(this.sentFrom);
    }

    private void buildRegistrationSuccess()
    {
        try {

            boolean result = db.performDBSearch(this.action);

            if(result) {
                Update update = new Update();
                update.communicationType = "registrationSuccess";
                update.userEmail = action.userEmail;
                update.userName = action.userName;
                update.successMessage = "User account has been successfully created.";
                this.update = update;
            }

        } catch (Exception e){
            System.out.println(e);
        }

        this.sendTo.add(this.sentFrom);

    }

    private void buildBeginNewMatch()
    {
      this.update.communicationType = "beginNewMatch";
      this.update.matchID = "dummy_math_ID";
      this.update.initialBoard = new int[5][5];
      this.update.initialBoard[0][0] = 1;
      this.update.initialBoard[0][1] = 2;
      this.update.whoseTurn = "opponent";
      this.update.matchBeginTime = "dummy_match_begin_time";
        this.sendTo.add(this.sentFrom);
    }

    private void buildInvitation()
    {
        this.update.communicationType = "invitation";
        this.update.invitationFrom = "player1";
        this.update.invitationTo = "player2";
        this.update.invitationTime = "dummy_time";
        this.sendTo.add(this.sentFrom);
    }

    private void buildEndMatch()
    {
        this.update.communicationType = "endMatch";
        this.update.matchID = "dummy_match_ID";
        this.update.endCondition = "quit";
        this.update.winnerName = "player1";
        this.update.loserName = "player2";
        this.update.matchEndTime = "dummy_end_time";
        this.sendTo.add(this.sentFrom);
    }

    private void buildLoginSuccess()
    {
        this.update.communicationType = "loginSuccess";
        this.update.invitations = null;
        this.update.matchesInProgress = null;
        this.update.matchesCompleted = null;
        this.sendTo.add(this.sentFrom);
    }

    public Update getUpdate()
    {
        return this.update;
    }
    public WebSocket getSentFrom() {return this.sentFrom;}
    public ArrayList<WebSocket> getSendTo() {return this.sendTo;}

}
