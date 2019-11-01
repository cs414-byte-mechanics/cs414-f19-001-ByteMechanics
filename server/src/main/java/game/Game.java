package game;

import database.*;
import webconnection.*;
import java.util.*;

public class Game {
    private DatabaseHandler dbHandler;
    
    public Game(){
        dbHandler = new DatabaseHandler();
    }


    public void createNewGame(Action action) throws Exception {
        //Initialize board and pieces
        GameBoard board = new GameBoard();
        board.initialize();
        
        //Save to database
        int matchID = dbHandler.addNewGame(action, board.getBoardForDatabase());
    }
    
    public void loadExistingGame(Action action) throws Exception {
        GameBoard gameBoard = new GameBoard();       
        
        //get game with that matchID from database
        String[][] board = dbHandler.retrieveGameInfo(action);
        
        //load game with that matchID from database
        gameBoard.loadGame(board);
        //System.out.println(gameBoard);
        
        //performMove
    }
    
    public void performMove(GameBoard board, int startLocation, int endLocation){
        //get river dwellers
        
        //actually perform move 
        
        //get river dwellers again & drown any in river
    }

}
