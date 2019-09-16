package Game;

public class GameBoard extends GamePiece {

    GamePiece board[][] = new GamePiece[7][7]; // every single board square has GamePiece attribute

    public GameBoard(){
        /* initialize every board square to NULL */
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++) {
                board[i][j] = null;
            }
        }
    }



}
