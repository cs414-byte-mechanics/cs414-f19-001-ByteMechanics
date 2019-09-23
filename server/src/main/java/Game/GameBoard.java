package Game;

import Game.GamePiece;
import Game.PawnPiece;

public class GameBoard{
    static int riverRow = 3;  /* river is on row 3 in the board */
    static int boardNumRows = 7;
    static int boardNumCols = 7;

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
    MonkeyPiece monkeyP2;
    CrocodilePiece crocodileP2;
    ZebraPiece zebraP2;
    ElephantPiece elephant1P2;
    ElephantPiece elephant2P2;
    LionPiece lionP2;


    /* playing board with 49 squares */
    GamePiece board[][] = new GamePiece[boardNumRows][boardNumCols];

    public GameBoard(){
     /* initialize every board square to NULL */
        for (int i = 0; i < boardNumRows; i++){
            for (int j = 0; j < boardNumCols; j++) {
                board[i][j] = null;
            }
        }
    }

    public void InitGameBoard(){
        /* Create and setup game pieces for player 1 */
        giraffeP1 = new GiraffePiece(0, 0, 1);
        board[0][0] = giraffeP1 ;
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
        for (int i =0; i<=6; i++){
            board[1][i] = new PawnPiece(1, i, 1);
        }


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

        for (int i =0; i <=6; i++){
            board[5][i] = new PawnPiece(5, i, 2 );
        }

    }


    public String toString(){
        String row = "";
        String boardStr = "--------------\n";
        String playPiece;
        for (int i = boardNumRows - 1; i >= 0; i--){
            row = "|";
            for (int j = boardNumCols - 1; j >= 0; j--){
                GamePiece piece = board[i][j];
                if (piece instanceof CrocodilePiece)
                        playPiece = (piece.player == 1) ? "c" : "C";
                else if (piece instanceof ElephantPiece)
                        playPiece = (piece.player == 1) ? "e" : "E";
                else if (piece instanceof GiraffePiece)
                        playPiece = (piece.player == 1) ? "g" : "G";
                else if (piece instanceof LionPiece)
                        playPiece = (piece.player == 1) ? "l" : "L";
                else if (piece instanceof MonkeyPiece)
                        playPiece = (piece.player == 1) ? "m" : "M";
                else if (piece instanceof ZebraPiece)
                        playPiece = (piece.player == 1) ? "z" : "Z";

                    //case PawnPiece: playPiece = (piece.player == 1) ? "p" : "P";
                    //  break;
                else
                    playPiece = " ";

                row = row + playPiece + "|";
            }
            row = row + "\n";
            row = row + "--------------\n";
            boardStr = boardStr + row;
        }

        return boardStr;
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){

        /* routine does NO error checking but assumes move is legal and updates the piece's info
           as well as set it's previous square location to NULL */

        GamePiece playerPiece = this.board[fromRow][fromCol];
        boolean startInRiver = playerPiece.inRiver();

        this.board[toRow][toCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = null;
        this.board[toRow][toCol].row = toRow;
        this.board[toRow][toCol].column = toCol;

        boolean endInRiver = playerPiece.inRiver();

        if (!(this.board[toRow][toCol] instanceof MonkeyPiece) &&
                !(this.board[toRow][toCol] instanceof CrocodilePiece) &&
                /* a game piece other than monkey or crocodile started and ended in the river */
                /* monkey must do checking after it's full sequence of moves */
                /* crocodile can stay in the water always */
                startInRiver && endInRiver) {
            /* need to remove the piece since it drowned */
            /* can call capture() when we implement it */
        }


        // when move a piece, if it is pawn and in land in row 6, we should turn on the super pawn flag
        if ( (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==1 && this.board[toRow][toCol].row == 6 ) ||
        (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==2 && this.board[toRow][toCol].row == 0 ) ) {

        PawnPiece pawnPiece = (PawnPiece) this.board[toRow][toCol];
        pawnPiece.superPawn = true;

        }
    }
}
