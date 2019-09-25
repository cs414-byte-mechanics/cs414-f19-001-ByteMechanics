package Game;

import Game.GameBoard;
import Game.Player;

public class Game {
    int activePlayer;
    Player player1;
    Player player2;


    public Game(){
        activePlayer = 1;
        //player1 = new Player(1);
        //player2 = new Player(2);
        GameBoard gameboard = new GameBoard();
    }

    public void alternatePlayers(){
        /* change which player is now actively making a move */
        activePlayer = (activePlayer == 1) ? 2 : 1;
    }

}
