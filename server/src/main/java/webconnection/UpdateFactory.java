package webconnection;
import database.*;
import Game.*;
import java.util.List;
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
            case "quitMatch": return this.abandonGame(action);
            case "unregisterUser": return this.unregisterUser(action);
            case "attemptLogin": return this.logIn(action);
            case "attemptLogout": return this.buildLogoutSuccess(action);
            case "searchUser": return this.buildSearchResult(action);
            case "sendInvitation": return this.buildInvitationSentStatus(action);
            case "searchGames": return this.buildSearchGamesResult(action);
            case "getUserInvsLists": return this.buildSendUserInvsLists(action);
            case "rejectInvite": return this.buildInviteRejectStatus(action);
            case "requestGameLoad": return this.retrieveSingleGame(action);
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
                    update.communicationType = lionExist ? "updateBoard" : "endMatch";
                    update.statusMessage = lionExist ? "The player's move was valid and the board has been updated" : "Lion is captured, Game is Over!";
                    update.endCondition = lionExist ? "active" : "won";
                    update.matchID = action.matchID;
                    update.playerName = action.playerName ;
                    update.pieceID =  action.pieceID ;
                    update.updatedBoard = gameBoard.getBoardForDatabase();
                    updateTurn(update, action, game);

                    update.winnerName = action.playerName = game.getActivePlayer(); /*this might need to be replace with action.playerName later*/
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
        } catch(Exception e) {}

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
        } catch(Exception e) {
            return new ServerError(-1, "Error in trying to reject invitation.");
        }
        update.statusMessage = "invite rejection complete";
        return update;
    }
    
    private Update retrieveSingleGame(Action action){
        
        Update update = new Update();
        
        
        
        return update;
    }

}
