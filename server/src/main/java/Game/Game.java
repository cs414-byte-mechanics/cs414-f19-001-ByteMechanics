package Game;

import database.*;
import webconnection.*;

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
    
    public void loadExistingGame(String matchID){
        GameBoard board = new GameBoard();       
        
        //get game with that matchID from database
        
        //load game with that matchID from database
        //board.loadGame(currentBoard);
        
        //performMove
    }
    
    public void performMove(GameBoard board, int startLocation, int endLocation){
        //get river dwellers
        
        //actually perform move 
        
        //get river dwellers again & drown any in river
    }

}
