package Game;

import database.*;
import webconnection.*;
import java.util.*;

public class Game {
    private DatabaseHandler dbHandler;
    private GameBoard gameBoard;
    
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
    }
    
    public void saveMatchState(int matchID, String nextPlayer) throws Exception {
        dbHandler.saveGameState(matchID, nextPlayer, gameBoard.getBoardForDatabase());
    }
    
    public String[][] getBoard(){
        return gameBoard.getBoardForDatabase();
    }

    public GameBoard getGameBoard() {return gameBoard;}

    /* this function extract desired move and validate that the move from current location to destination is valid or no */
    public boolean processMove(int[] desiredMove, GameBoard congoGame){ // OK -- move this to Game instead of perfprm move
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

            return (piece.performMove(movesRow, movesCol, congoGame));
        }

        return false;
    }


    public boolean moveSequenceCorrect(Action action, GameBoard board, int location){
        /* which player moved the piece */
        int activePlayer = (action.playerName.compareTo(action.playerOneName) == 0) ? 1 : 2;
        /* which player owns the piece to be moved */
        try {
            int pieceOwner = board.getGamePiece(GameBoard.getRow(location), GameBoard.getCol(location)).player;
            return (activePlayer == pieceOwner);
        }
        catch (Exception e){
            /* catch when there is no piece on the board */
            /* then this is not a valid sequence of moves for either player */
            return false;
        }
    }
}
