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
    
    public boolean performMove(int pieceLocation, ArrayList<Integer> destRows, ArrayList<Integer> destCols){
        int pieceCol = pieceLocation % 10;
        int pieceRow = pieceLocation /10;
        GamePiece pieceToMove = gameBoard.getGamePiece(pieceRow, pieceCol);
    
        return pieceToMove.performMove(destRows, destCols, gameBoard);
    }
    
    public void saveMatchState(int matchID) throws Exception {
        dbHandler.saveGameState(matchID, gameBoard.getBoardForDatabase());
    }
    
    public String[][] getBoard(){
        return gameBoard.getBoardForDatabase();
    }

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

}
