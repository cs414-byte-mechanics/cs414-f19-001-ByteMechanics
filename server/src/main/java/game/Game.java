package Game;

import database.*;
import webconnection.*;
import java.util.*;

public class Game {
    private DatabaseHandler dbHandler;
    private GameBoard gameBoard;
    private String activePlayer;
    private String winningPlayer;
    
    public Game(){
        dbHandler = new DatabaseHandler();
        gameBoard = new GameBoard();
    }

    public int createNewGame(Action action) throws Exception {
        //Initialize board and pieces
        gameBoard.initialize();

        //Save to database
        int matchID = dbHandler.addNewGame(action, gameBoard.getBoardForDatabase());

        return matchID;
    }
    
    public void loadExistingGame(Action action) throws Exception {
        //get game with that matchID from database
        String[][] board = dbHandler.retrieveGameInfo(action);
        
        //load game with that matchID from database
        gameBoard.loadGame(board);

        //get active player that should make the next move
        activePlayer = dbHandler.retrieveActivePlayerInfo(action);
        //get winning player
        winningPlayer = dbHandler.retrieveWinnerInfo(action);
    }
    
    public void saveMatchState(int matchID) throws Exception {
        dbHandler.saveGameState(matchID, this.getActivePlayer(), gameBoard.getBoardForDatabase(), this.getWinningPlayer());
    }
    
    public String[][] getBoard() throws Exception {
        return gameBoard.getBoardForDatabase();
    }

    public GameBoard getGameBoard() {return gameBoard;}

    public String getActivePlayer() {return activePlayer;}

    public void setActivePlayer(String player) {activePlayer = player;}

    public String getWinningPlayer() {return winningPlayer;}

    public void setWinningPlayer(String winner) {winningPlayer = winner;}

    /* this function extract desired move and validate that the move from current location to destination is valid or no */
    public void processMove(int[] desiredMove, GameBoard congoGame) throws Exception { // OK -- move this to Game instead of perfprm move
        ArrayList<Integer> movesRow = new ArrayList<>();
        ArrayList<Integer> movesCol = new ArrayList<>();

        if (desiredMove != null) {
            /* Extract current location*/
            int pieceCol = desiredMove[0] % 10;
            int pieceRow = desiredMove[0] / 10;

            /* Grab the piece based on the current location */
            GamePiece piece =  congoGame.getGamePiece(pieceRow, pieceCol);

            /* Extract destination*/
            for (int i = 1; i < desiredMove.length; i++) {
                int col = desiredMove[i] % 10;
                int row = (desiredMove[i] - col) / 10;
                movesCol.add(col);
                movesRow.add(row);
            }

            piece.performMove(movesRow, movesCol, congoGame);
        }
    }


    public boolean moveSequenceCorrect(Action action, Game game, int location) throws Exception{
        /* which player moved the piece */
        String whoseTurn = game.getActivePlayer();
        GameBoard board = game.getGameBoard();
        int activePlayer = (whoseTurn.compareTo(action.playerOneName) == 0) ? 1 : 2;
        int requestingPlayer = (action.userName.compareTo(action.playerOneName) == 0) ? 1 : 2;

        /* which player owns the piece to be moved */
        int pieceOwner;
        try {
            pieceOwner = board.getGamePiece(GameBoard.getRow(location), GameBoard.getCol(location)).getPlayer();
        }
        catch (Exception e){
            /* catch when there is no piece on the board */
            /* then this is not a valid sequence of moves for either player */
            throw new Exception("Player moving a non-existant game piece.");
        }
        if(pieceOwner!=requestingPlayer) throw new Exception("Player cannot move opponent's pieces.");
        return (activePlayer == pieceOwner);

    }
}
