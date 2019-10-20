package Game;

import Game.GamePiece;

import static Game.GameBoard.boardNumCols;

public class Player {

    // every team has 14 single pieces, including 7 animals and 7 pawns
    public static int numberOfPieces= 14;

    // to identify which player it is? one or two?
    int playerID;

    // An array to store every player's pieces (totally 14) - every single piece is a game piece class like Pawn piece
    GamePiece playerPieces[] = new GamePiece[numberOfPieces];

    public Player (int id ){

        playerID = id;
    }

    public void initPlayerPieces(GameBoard board){

        int homeRow = (playerID == 1) ? 0 : 6;
        int dir = (playerID == 1) ? 1 : -1;

        int i = 0;  /* index to track the number of pieces */
        for (int r = homeRow; r !=  homeRow + (2*dir);r= r + dir) {
            for (int c = 0; c < boardNumCols; c++) {
                playerPieces[i] = board.getGamePiece(r, c);
                i++;
            }
        }
    }
}
