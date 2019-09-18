package Game;

import Game.GamePiece;

public class GameBoard{
    static int riverRow = 3;  /* river is on row 3 in the board */

    /* player1's pieces */
    GiraffePiece giraffeP1;
    MonkeyPiece monkeyP1;
    CrocodilePiece crocodileP1;
    ZebraPiece zebraP1;
    ElephantPiece elephant1P1;
    ElephantPiece elephant2P1;
    LionPiece lionP1;

    /* player2's pieces */
    GiraffePiece giraffeP2;
    CrocodilePiece crocodileP2;
    MonkeyPiece monkeyP2;
    ZebraPiece zebraP2;
    ElephantPiece elephant1P2;
    ElephantPiece elephant2P2;
    LionPiece lionP2;

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
        giraffeP1 = new GiraffePiece(0, 0, 1);
        board[0][0] = giraffeP1;
        monkeyP1 = new MonkeyPiece(0,1,1);
        board[0][1] = monkeyP1;
        elephant1P1 = new ElephantPiece(0,2,1);
        board[0][2] = elephant1P1;
        lionP1 = new LionPiece(0,3,1);
        board[0][3] = lionP1;
        elephant2P1 = new ElephantPiece(0,4,1);
        board[0][4] = elephant2P1;
        crocodileP1 = new CrocodilePiece(0,5, 1);
        board[0][5] = crocodileP1;
        zebraP1 = new ZebraPiece(0, 6, 1);
        board[0][6] = zebraP1;
        /* need to initialize all pawns */

        /* Create and setup game pieces for player 2 */
        giraffeP2 = new GiraffePiece(6, 0, 2);
        board[6][0] = giraffeP2;
        monkeyP2 = new MonkeyPiece(6, 1, 2);
        board[6][1] = monkeyP2;
        elephant1P2 = new ElephantPiece(6,2,2);
        board[6][2] = elephant1P2;
        lionP2 = new LionPiece(6,3,2);
        board[6][3] = lionP2;
        elephant2P2 = new ElephantPiece(6,4,2);
        board[6][4] = elephant2P2;
        crocodileP2 = new CrocodilePiece(6, 5, 2);
        board[6][5] = crocodileP2;
        zebraP2 = new ZebraPiece(6, 6, 2);
        board[6][6] = zebraP2;
        /* need to initialize all pawns */

    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){
        /* routine does NO error checking but assumes move is legal and updates the piece's info
           as well as set it's previous square location to NULL */
        this.board[toRow][toCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = null;
        this.board[toRow][toCol].row = toRow;
        this.board[toRow][toCol].column = toCol;
    }

}
