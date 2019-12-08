package webconnection;
import database.*;
import Game.*;

import org.java_websocket.WebSocket;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import java.util.Properties;
import javax.mail.PasswordAuthentication;

public class UpdateFactory
{
    private DatabaseHandler db;
    private WebsocketServer wss;

    public UpdateFactory(WebsocketServer wss) {
        this.db = new DatabaseHandler();
        this.wss = wss;
    }

    public Update getUpdate(Action action, WebSocket conn) {
        //decide which update to construct given the type of action sent from the client
        switch(action.communicationType)
        {
            case "requestMoves": return this.buildUpdateBoard(action);
//            case "requestUpdate": return this.buildUpdateBoard(action);
            case "registerUser": return this.registerUser(action);
            case "requestBeginNewMatch": return this.createNewMatch(action);
            case "invitation": return this.buildInvitation();
            case "invitationResponse": return null;
            case "quitMatch": return this.abandonGame(action);
            case "unregisterUser": return this.unregisterUser(action);
            case "attemptLogin": return this.logIn(action, conn);
            case "attemptLogout": return this.logOut(action);
            case "searchUser": return this.buildSearchResult(action);
            case "sendInvitation": return this.buildInvitationSentStatus(action);
            case "searchGames": return this.buildSearchGamesResult(action);
            case "getUserInvsLists": return this.buildSendUserInvsLists(action);
            case "rejectInvite": return this.buildInviteRejectStatus(action);
            case "requestGameLoad": return this.retrieveSingleGame(action);
            case "acceptInvite": return this.buildInviteAcceptStatus(action);
            default:
                System.err.println("Invalid action communication type.");
                return new Update();
        }
    }

    private void updateTurn(Update update, Action action, Game game){
        String nextPlayer;
        if (update.communicationType.compareTo("updateBoard") == 0) {
            /* swap which player is taking a turn next otherwise leave things unchanged */
            nextPlayer = (game.getActivePlayer().compareTo(action.playerOneName) == 0) ? action.playerTwoName : action.playerOneName;

            update.whoseTurn = nextPlayer;
            update.playerName = nextPlayer;
            game.setActivePlayer(nextPlayer);

        }
    }

    private Update buildUpdateBoard(Action action) {
        try {
            boolean lionExist;
            Game game = new Game();
            game.loadExistingGame(action);
            GameBoard gameBoard = game.getGameBoard();
            Update update = new Update();

            if (!(game.moveSequenceCorrect(action, game, action.desiredMoves[0]))) {
                throw new Exception(game.getActivePlayer() + " should be making a move"); }

            /** At this point we track if opponent's lion is in castle, and in this case we can still play and perform move and keep playing, otherwise lion is captured and keep playing does not make sense!!!!*/
            lionExist = gameBoard.lionInCastle(gameBoard.getBoardForDatabase(), action.desiredMoves[0]); /* because we have not performed move yet, so piece's location is desired[0] */
            if (lionExist){
                try
                {
                    game.processMove(action.desiredMoves, gameBoard);

                    /** after performing valid move, we need to check if lion is till in castle or it is captured?*/
                    lionExist = gameBoard.lionInCastle(gameBoard.getBoardForDatabase(), action.desiredMoves[1]); /* at this point, move is performed, so piece location is updated and is desired[1]. */
                    update.recipients = new String[] {action.playerOneName, action.playerTwoName};
                    update.communicationType = lionExist ? "updateBoard" : "endMatch";
                    update.statusMessage = lionExist ? "The player's move was valid and the board has been updated" : "Lion is captured, Game is Over!";
                    update.endCondition = lionExist ? "active" : "won";
                    update.matchID = action.matchID;
                    update.playerName = action.playerName ;
                    update.pieceID =  action.pieceID ;
                    update.playerOneName = action.playerOneName;
                    update.playerTwoName = action.playerTwoName;
                    update.updatedBoard = gameBoard.getBoardForDatabase();
                    updateTurn(update, action, game);

                    update.winnerName = (lionExist) ? "" : game.getActivePlayer();
                    if (update.endCondition.compareTo("won") == 0)
                        game.setWinningPlayer(update.winnerName);

                    game.saveMatchState(Integer.parseInt(action.matchID));

                    return update;
                }
                catch (Exception e){
                    e.printStackTrace();
                    ServerError error = new ServerError(102, e.getMessage());
                    return error; }}
            else
                throw new Exception("Game Is Over - Opponent's Lion is Captured");

        } catch (Exception e){
            e.printStackTrace();
            ServerError error = new ServerError(102, e.getMessage());
            return error;
        }
    }

    private Update unregisterUser(Action action) {
        try {
            db.unregisterUser(action);
            Update update = new Update();
            update.communicationType = "unregistrationSuccess";
            update.userEmail = action.userEmail;
            update.userName = action.userName;
            update.statusMessage = "User account has been unregistered.";

            return update;

        } catch (Exception e){
            System.out.println(e);
            ServerError error = new ServerError(101, e.getMessage());
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

    private Update logIn(Action action, WebSocket conn) {
        try {
            Update update = new Update();
            update.userName = db.attemptLogin(action);
            update.userEmail = action.userEmail;
            update.communicationType = "loginSuccess";

            wss.addSession(update.userName, conn);
            return update;

        } catch (Exception e){
            System.out.println(e);
            ServerError error = new ServerError(100, e.getMessage());
            return error;
        }
    }

    private Update logOut(Action action) {
        Update update = new Update();
        update.communicationType = "logoutSuccess";
        update.statusMessage = "User has successfully logged out.";
        wss.removeSession(action.userName);
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

    private Update abandonGame(Action action) {
        try {
            Update update = new Update();
            update.communicationType = "endMatch";
            update.matchID = action.matchID;
            update.endCondition = "quit";
            update.winnerName = db.abandonActiveGame(action);
            update.loserName = action.playerQuitting;
            //We get the match end time in the database, but we can figure this out
            update.matchEndTime = "dummy_end_time";
            return update;
        } catch(Exception e){
            System.out.println(e);
            ServerError error = new ServerError(104, e.getMessage());
            return error;
        }
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

    private Update buildSearchGamesResult(Action action) {
        Update update = new Update();
        update.communicationType = "searchGamesResult";
        update.userName= action.userName;
        try {
            update.searchResults = db.searchGames(action);
        } catch(Exception e) {
            e.printStackTrace();
            return new ServerError(-1, e.getMessage());
        }

        return update;
    }
  
    private Update buildInvitationSentStatus(Action action) {
        Update update = new Update();
        update.communicationType = "invitationSentStatus";
        update.statusMessage = "Invitation sent to " + action.invitationTo + "!";
        try {
            db.sendGameInvitation(action);
        } catch(Exception e) {
            return new ServerError(-1, e.getMessage());
        }
        return update;
    }

    private Update buildSendUserInvsLists(Action action) {
        Update update = new Update();
        update.communicationType = "sendUserInvsLists";
        ArrayList<List<String>> invitationLists = new ArrayList<>();
        try {
            invitationLists = db.getInvitationLists(action);
        } catch(Exception e) {
            return new ServerError(-1, e.getMessage());
        }
        update.sentToNames = invitationLists.get(0);
        update.sentToTimes = invitationLists.get(1);
        update.receivedFromNames = invitationLists.get(2);
        update.receivedFromTimes = invitationLists.get(3);
        return update;
    }

    private Update buildInviteRejectStatus(Action action) {
        Update update = new Update();
        update.communicationType = "inviteRejectStatus";
        try {
            db.removeInvitation(action);

            //Get email associated with user that sent invite
            String invitorEmail = db.retrieveEmailForUser(action.invitationFrom);
            System.out.println(invitorEmail);
            
            //Send notification to user that sent invite
            sendEmailNotification( action.userName + " rejected your invitation to start a game on CongoOnline. Log in to invite another friend!", invitorEmail, action.userName);
            
        } catch(Exception e) {
            return new ServerError(-1, "Error in trying to reject invitation.");
        }
        update.statusMessage = "invite rejection complete";
        return update;
    }

    /**
     Returns status of game using the values expected by the client
     */
    private String convertGameStatus(String databaseStatus){
        String statusForClient = "";
        if (databaseStatus.compareTo("in progress") == 0) {
            statusForClient = "active";
        }
        else if (databaseStatus.compareTo("abandoned") == 0) {
            statusForClient = "quit";
        }
        else if (databaseStatus.compareTo("finished") == 0) {
            statusForClient = "won";
        }
        return statusForClient;
    }
    
    private Update retrieveSingleGame(Action action){
        try {
            ResultSet results = db.getGameInfo(action.matchID);
            if (!results.next()) return new ServerError(105, "No game exists with this match ID.");

            Update update = new Update();
            String boardAsString = results.getString("board");
            String[][] board = new String[7][7];
            
            int index = 0;
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    board[i][j] = Character.toString(boardAsString.charAt(index));
                    index++;
                }
            }

            update.communicationType = "updateBoard";
            update.updatedBoard = board;
            update.matchID = action.matchID;
            update.whoseTurn = results.getString("next_turn");
            update.playerName = results.getString("next_turn");
            update.playerOneName = results.getString("p1");
            update.playerTwoName = results.getString("p2");
            update.endCondition = convertGameStatus(results.getString("status"));

            if(!(action.userName.equals(update.playerOneName) || action.userName.equals(update.playerTwoName))) throw new Exception();

            return update;
        
        } catch(Exception e){
            return new ServerError(105, "Game information cannot be retrieved");
        }
    }

   private Update buildInviteAcceptStatus(Action action) {
        Update update = new Update();
        Action createGameAction = new Action();
        createGameAction.communicationType = "requestBeginNewMatch";
        createGameAction.playerOneName = action.userName;
        createGameAction.playerTwoName = action.invitationFrom;
        try {
            this.createNewMatch(createGameAction);
            
            //Get email associated with this user
            String invitorEmail = db.retrieveEmailForUser(action.invitationFrom);
            
            //Send notification to user that sent invite
            sendEmailNotification( action.userName + " accepted your invitation to start a game on CongoOnline. Log in to make the first move!", invitorEmail, action.userName);

        } catch(Exception e) {
            System.err.println("New match cannot be created");
            return new ServerError(-1, "Error in creating new game.");
        }
        try  {
            db.removeInvitation(action);
        } catch (Exception e) {
            System.err.println("Error in removing invitation");
            return new ServerError(-1, "Error in accepting invitation");
        }
        update.communicationType = "inviteAcceptStatus";
        update.statusMessage = "invitation acceptance completed";
        return update;
   }
   
   private boolean sendEmailNotification(String emailBody, String recipientEmail, String opponent){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("congoOnlineTeam", "byteMechanics414");
                    }
                });
                
        try {
            MimeMessage message = new MimeMessage(session);
            message.setContent(emailBody, "text/plain");
            message.setSubject(opponent + " has responded to your invitation", "text/plain");
            message.setFrom(new InternetAddress(recipientEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            Transport.send(message);
            return true;
        } catch (Exception e){
            return false;
        }
   }
}
