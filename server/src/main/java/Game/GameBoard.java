package Game;

import Game.GamePiece;
import Game.PawnPiece;
import Game.Player;

public class GameBoard{
    static int riverRow = 3;  /* river is on row 3 in the board */
    static int boardNumRows = 7;
    static int boardNumCols = 7;

    /* playing board with 49 squares */
    GamePiece board[][] = new GamePiece[boardNumRows][boardNumCols];

    public GameBoard(){
     /* initialize every board square to NULL */
        for (int i = 0; i < boardNumRows; i++){
            for (int j = 0; j < boardNumCols; j++) {
                board[i][j] = null;
            }
        }
        /* this assumes the players have already set up the pieces that we're placing on the board */
    }

    public void InitGameBoard(){
        /* Create and setup game pieces for player 1 */
        board[0][0] = new GiraffePiece(0, 0, 1);
        board[0][1] = new MonkeyPiece(0,1,1);
        board[0][2] = new ElephantPiece(0,2,1);
        board[0][3] = new LionPiece(0,3,1);
        board[0][4] = new ElephantPiece(0,4,1);
        board[0][5] = new CrocodilePiece(0,5, 1);
        board[0][6] = new ZebraPiece(0, 6, 1);

        /* need to initialize all pawns */
        for (int i =0; i<=6; i++){
            board[1][i] = new PawnPiece(1, i, 1);
        }

        /* Create and setup game pieces for player 2 */
        board[6][0] = new GiraffePiece(6, 0, 2);
        board[6][1] = new MonkeyPiece(6, 1, 2);
        board[6][2] = new ElephantPiece(6,2,2);
        board[6][3] = new LionPiece(6,3,2);
        board[6][4] = new ElephantPiece(6,4,2);
        board[6][5] = new CrocodilePiece(6, 5, 2);
        board[6][6] = new ZebraPiece(6, 6, 2);

        /* need to initialize all pawns */
        for (int i =0; i <=6; i++){
            board[5][i] = new PawnPiece(5, i, 2 );
        }

    }

    public void initGameBoardForPlayer(Player player) {
        /* Create and setup game pieces for player */
        int row = (player.playerID == 1) ? 0 : 6;
        int increment = (player.playerID == 1) ? 1 : -1;
        /* need to initialize all animal pieces */
        for (int i =0; i<=6; i++){
            board[row][i] = player.playerPieces[i];
        }
        row = row + increment;
        /* need to initialize all pawns */
        for (int i =0; i<=6; i++){
            board[row][i] = player.playerPieces[i + boardNumCols];
        }
    }

    public String toString(){
        String row = "";
        String boardStr = "--------------\n";
        String playPiece;
        for (int i = boardNumRows - 1; i >= 0; i--){
            row = "|";
            for (int j = 0; j < boardNumCols; j++){
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
                else if (piece instanceof PawnPiece)
                        playPiece = (piece.player == 1) ? "p" : "P";
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

    public GamePiece getGamePiece(int row, int col){
        return this.board[row][col];
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){

        /* routine does NO error checking but assumes move is legal and updates the piece's info
           as well as set it's previous square location to NULL */

        //GamePiece boardPiece = getGamePiece(fromRow, fromCol);
        GamePiece boardPiece = this.board[fromRow][fromCol];
        boolean startInRiver = boardPiece.inRiver();

        this.board[toRow][toCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = null;
        /* update the info in GamePiece */
        /* since Player.playerPiece[] is point at this same object it is updated as well */
        this.board[toRow][toCol].row = toRow;
        this.board[toRow][toCol].column = toCol;

        boolean endInRiver = boardPiece.inRiver();

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

    public void capturePiece(GamePiece pieceToBeCaptured, GamePiece[] playerPiecesArray){
        // Capture is supposed to remove a piece from board, as well as piece's array
        System.out.println("Before any capture the piece's array is like "+playerPiecesArray);

        // extract the piece's column which is equal to index of piece in array
        int index = pieceToBeCaptured.column;

        // if it is not pawn, column number is equal to indexes
        if (!(pieceToBeCaptured instanceof PawnPiece))
            playerPiecesArray[index] = null ;

        else
            // if piece is pawn, index need to be added with 6
            playerPiecesArray[index + 7] = null;

        System.out.println("After capturing" + pieceToBeCaptured + "The array is like "+playerPiecesArray);

        // remove the piece from the board
        this.board[pieceToBeCaptured.row][pieceToBeCaptured.column] = null ;
    }
}
