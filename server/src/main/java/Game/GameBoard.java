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

    public GamePiece getGamePiece(int r, int c){
        if ((r >= 0) && (r < boardNumRows)
                && (c >= 0) && (c < boardNumCols)){
            return board[r][c];
        }
        else return null;
    }

    private void placePieces(int player){
        /* places all pieces on one side of the board for a specific player */
        int homeRow = (player == 1) ? 0 : 6;
        int pawnRow = (player == 1) ? 1 : 5;

        /* Create and setup animal game pieces for player */
        board[homeRow][0] = new GiraffePiece(homeRow, 0, player);
        board[homeRow][1] = new MonkeyPiece(homeRow,1,player);
        board[homeRow][2] = new ElephantPiece(homeRow,2,player);
        board[homeRow][3] = new LionPiece(homeRow,3,player);
        board[homeRow][4] = new ElephantPiece(homeRow,4,player);
        board[homeRow][5] = new CrocodilePiece(homeRow,5, player);
        board[homeRow][6] = new ZebraPiece(homeRow, 6, player);

        /* need to initialize all pawns */
        for (int i =0; i<=6; i++){
            board[pawnRow][i] = new PawnPiece(pawnRow, i, player);
        }
    }

    public void InitGameBoard(){
        /* Create and setup game pieces for player 1 */
        placePieces(1);

        /* Create and setup game pieces for player 2 */
        placePieces(2);

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

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){

        /* routine does NO error checking but assumes move is legal and updates the piece's info
           as well as set it's previous square location to NULL */

        //GamePiece boardPiece = getGamePiece(fromRow, fromCol);
        GamePiece boardPiece = this.board[fromRow][fromCol];
        boolean startInRiver = boardPiece.inRiver();

        /* capture piece that is in the destination square */
        if (this.board[toRow][toCol] != null) {
            /* This piece will remain in the player's list of pieces so we need to mark it as captured */
            this.board[toRow][toCol].setCaptured();
        }
        /* now move conquering piece onto the square */
        this.board[toRow][toCol] = this.board[fromRow][fromCol];
        this.board[fromRow][fromCol] = null;
        /* update the info in GamePiece */
        /* since Player.playerPiece[] is pointing at this same object it is updated as well */
        /* update the row,column variables in the GamePiece object to reflect new location */
        this.board[toRow][toCol].row = toRow;
        this.board[toRow][toCol].column = toCol;

        /* check if the piece ended its turn in the river to determine if it drowned */
        boolean endInRiver = boardPiece.inRiver();

        if (!(this.board[toRow][toCol] instanceof CrocodilePiece) &&
                /* if a game piece other than a crocodile started and ended in the river */
                startInRiver && endInRiver) {
            /* need to remove the piece since it drowned */
            /* can call capture() when we implement it */
            capturePiece(boardPiece);
        }


        // when move a piece, if it is pawn and in land in row 6, we should turn on the super pawn flag
        if ( (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==1 && this.board[toRow][toCol].row == 6 ) ||
        (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==2 && this.board[toRow][toCol].row == 0 ) ) {

        PawnPiece pawnPiece = (PawnPiece) this.board[toRow][toCol];
        pawnPiece.superPawn = true;

        }
    }

    public void capturePiece(GamePiece pieceToBeCaptured){
//        // Capture is supposed to remove a piece from board, as well as piece's array
//        System.out.println("Before any capture the piece's array is like "+playerPiecesArray);
//
//        // extract the piece's column which is equal to index of piece in array
//        int index = pieceToBeCaptured.column;
//
//        // if it is not pawn, column number is equal to indexes
//        if (!(pieceToBeCaptured instanceof PawnPiece))
//            playerPiecesArray[index] = null ;
//
//        else
//            // if piece is pawn, index need to be added with 6
//            playerPiecesArray[index + 7] = null;
//
//        System.out.println("After capturing" + pieceToBeCaptured + "The array is like "+playerPiecesArray);
        this.board[pieceToBeCaptured.row][pieceToBeCaptured.column].setCaptured();

        // remove the piece from the board
        this.board[pieceToBeCaptured.row][pieceToBeCaptured.column] = null ;
    }
}
