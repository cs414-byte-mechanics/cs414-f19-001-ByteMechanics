package Game;

import Game.GameBoard;
import Game.Player;

public class Game {
    int activePlayer;
    Player player1;
    Player player2;
    GameBoard gameBoard;

    public Game(){
        activePlayer = 1;
        gameBoard = new GameBoard();
        gameBoard.InitGameBoard();
        player1 = new Player(1);
        player2 = new Player(2);
        player1.initPlayerPieces(gameBoard);
        player2.initPlayerPieces(gameBoard);
    }

    public void alternatePlayers(){
        /* change which player is now actively making a move */
        activePlayer = (activePlayer == 1) ? 2 : 1;
    }

}
