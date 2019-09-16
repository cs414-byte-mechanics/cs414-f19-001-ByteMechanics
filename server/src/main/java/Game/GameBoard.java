package Game;

public class GameBoard extends GamePiece {
    /* player1's pieces */
    GiraffePiece giraffeP1;
    MonkeyPiece monkeyP1;
    CrocodilePiece crocodileP1;
    ZebraPiece zebraP1;

    /* player2's pieces */
    GiraffePiece giraffeP2;
    MonkeyPiece monkeyP2;
    CrocodilePiece crocodileP2;
    ZebraPiece zebraP2;

    /* playing board with 49 squares */
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
        //instantiate piece on board
        monkeyP1 = new MonkeyPiece(0,1,1);
        board[0][1] = monkeyP1;
        crocodileP1 = new CrocodilePiece(0,5, 1);
        board[0][5] = crocodileP1;
        zebraP1 = new ZebraPiece(0, 6, 1);
        board[0][6] = zebraP1;

        /* Create and setup game pieces for player 2 */
        giraffeP2 = new GiraffePiece();
        //instantiate piece on board
        monkeyP2 = new MonkeyPiece(6, 1, 2);
        board[6][1] = monkeyP2;
        crocodileP2 = new CrocodilePiece(6, 5, 2);
        board[6][5] = crocodileP2;
        zebraP2 = new ZebraPiece(6, 6, 2);
        board[6][6] = zebraP2;

    }

}
