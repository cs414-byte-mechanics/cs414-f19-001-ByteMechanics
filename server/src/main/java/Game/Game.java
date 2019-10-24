package Game;

public class Game {
    int activePlayer;

    public Game(){
        activePlayer = 1;
    }

    public void alternatePlayers(){
        /* change which player is now actively making a move */
        activePlayer = (activePlayer == 1) ? 2 : 1;
    }
    
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
    }

}
