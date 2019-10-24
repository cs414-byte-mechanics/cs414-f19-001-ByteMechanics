package Game;

import Game.GameBoard;
import Game.Player;

public class Game {

    public void createNewGame(){
        GameBoard board = new GameBoard();
        board.initialize();
        
        //save game info into database
    }
    
    public void loadExistingGame(String matchID){
        GameBoard board = new GameBoard();       
        
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
