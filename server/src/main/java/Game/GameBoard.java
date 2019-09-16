package Game;

public class GameBoard extends GamePiece {
    GiraffePiece giraffeP1;
    MonkeyPiece monkeyP1;
    CrocodilePiece crocP1;
    ZebraPiece zebraP1;

    GamePiece board[][] = new GamePiece[7][7];

    public GameBoard(){
     /* initialize every board square to NULL */
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++) {
                board[i][j] = null;
            }
        }
    }

    public void InitGameBoard(){
        /* Create and setup game pieces for player 1 */
        giraffeP1 = new GiraffePiece();
        monkeyP1 = new MonkeyPiece(0,1,1);
        crocP1 = new CrocodilePiece(0,5, 1);
        zebraP1 = new ZebraPiece(0, 6, 1);

        /* Create and setup game pieces for player 2 */
        GiraffePiece giraffeP2 = new GiraffePiece();
        MonkeyPiece monkeyP2 = new MonkeyPiece(6, 1, 2);
        CrocodilePiece crocP2 = new CrocodilePiece(6, 5, 2);
        ZebraPiece zebraP2 = new ZebraPiece(6, 6, 2);
    }

}
