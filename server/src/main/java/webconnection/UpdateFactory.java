package webconnection;
import database.*;
import game.*;
import org.java_websocket.WebSocket;
import java.util.ArrayList;

public class UpdateFactory
{
    private DatabaseHandler db;
    private GameBoard congoGame;
    private int errorCode;
    private String communicationType , message, whoseTurn;
//    private String[][] boardToBeSent = congoGame.getBoardForDatabase(); /* current board has been stored*/

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
            default:
                System.err.println("Invalid action communication type.");
                return new Update();
        }
    }

    /* this function extract desired move and validate that the move from current location to destination is valid or no */
    public boolean processMove(int[] desiredMove, GameBoard congoGame){
        ArrayList<Integer> movesRow = new ArrayList<>();
        ArrayList<Integer> movesCol = new ArrayList<>();

        if (desiredMove != null) {
            /* Extract current location*/
            int pieceCol = desiredMove[0] % 10;
            int pieceRow = desiredMove[0] / 10;
//            System.out.println(" Piece is currently on " + pieceRow + pieceCol);

            /* Grab the piece based on the current location */
            GamePiece piece =  congoGame.getGamePiece(pieceRow, pieceCol);

            /* Extract destination*/
            for (int i = 1; i < desiredMove.length; i++) {
                int col = desiredMove[i] % 10;
                int row = (desiredMove[i] - col) / 10;
                movesCol.add(col);
                movesRow.add(row);
            }

            return (piece.performMove(movesRow, movesCol, congoGame));
        }

        return false;
    }

    /* helper routine to fill out message field*/
    private String constructMessage(String communicationType) {
        switch (communicationType){
            case "updateBoard": return  "The player's move was valid and the board has been updated";
            case "errorInvalidMove": errorCode= 102; return ServerError.getErrorMessage(errorCode);

            default:
                System.err.println("Message has not been constructed!!");
                return null;
        }
    }

    private String[][] updateBoard(String communicationType, String[][] board){
        if (communicationType == "updateBoard")
            board = congoGame.getBoardForDatabase();

        return board;
    }

    private String updateTurn (String communicationType){
        if(communicationType == "updateBoard")
            return "opponent";
        else
            return "you";
    }

    private Update wrapUpResponse(Update update, Action action, String communicationType, String message, String[][] board)
    {
        update.communicationType = communicationType;
        update.communicationVersion = 0;
        update.matchID = action.matchID = "dummy_match_ID";
        update.playerName = action.playerName = "dummy_playerName";
        update.pieceID =  action.pieceID = "dummy_pieceID";
        update.successMessage = message;

        /* updated board needs to be sent/returned to the client correctly*/
        update.updatedBoard = board ;
//        update.whoseTurn = turn;

        return update;
    }

    private Update constructResponse(Update update, Action action, String communicationType, String[][] board)
    {
        /*Construct response message according to communicationType*/
        String message = constructMessage(communicationType);

        /*Update board if communicationType is updateBoard */
        board = updateBoard(communicationType, board);

        /* switch the turn if move is valid and board get updated*/
//        String Turn = updateTurn(communicationType);

        /*Finally wrap up the response and send back to client*/
        update = wrapUpResponse(update, action, communicationType, message, board);

        return update;
    }

    private Update buildUpdateBoard(Action action) {
        try {
            Game game = new Game();
            game.loadExistingGame(action);

            String[][] boardToBeSent = congoGame.getBoardForDatabase(); /* current board has been stored*/
            Update update = new Update();

            boolean moveSucceeded = processMove(action.desiredMoves, congoGame);
            System.out.println("Request move is "+ moveSucceeded);

            if (moveSucceeded == true) /* move is validated/legal, so we need to return updated board back to client */ {
                communicationType = "updateBoard";
                constructResponse(update, action, communicationType, boardToBeSent);
            }
            else /* move isn't valid, so we return error */ {
                communicationType = "errorInvalidMove";
                constructResponse(update, action, communicationType, boardToBeSent);
            }
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

}
