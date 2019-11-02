package game;

import database.*;
import webconnection.*;
import java.util.*;
import game.pieces.*;

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

}
