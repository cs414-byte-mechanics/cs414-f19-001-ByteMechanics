package Game;

import Game.GameBoard;
import Game.Player;

import java.util.Arrays;

public class GamePiece {
    public int row;
    public int column;
    public int player;  /* set to 1 or 2 to indicate which player owns the piece */
    public boolean captured;

    public GamePiece(){
    }

    public GamePiece(int r, int c, int p){
        row = r;
        column = c;
        player = p;
        captured = false;
    }

    @Override
    public String toString() {
        return ("row: "+row+"\ncolumn: "+column+"\nplayer: "+player+"\ncaptured: "+captured+"\n");
    }

    public void setCaptured(){
        /* indicates that piece is captured and no longer in play on the board */
        captured = true;
    }

    public boolean checkCaptured(){
        /* returns a value indicating if piece is captured */
        return captured;
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board){
        /* ValidateMove should always be called on a specific GamePiece - monkey, lion, etc
        Therefore, this routine is returning false so it never validates and incorrect move.
        This needs to be here so the use of ValidateMove() in performMove() will compile.
         */
        return false;
    }

    public Boolean inRiver(){
        /* determines if playing piece is currently in the river */
        return inRiver(this.row);
    }

    public Boolean inRiver(int r){
        /* determines if specific location is in the river */
        if (r == GameBoard.riverRow)
            return true;
        else
            return false;
    }

    public Boolean squareEmptyOrCapturable(int row, int col, GamePiece[][] board){
        if (board[row][col] == null || board[row][col].player != this.player){
            /* square is open or contains opponent's piece */
            return true;
        }
        else {
            /* player tried landing on a square s/he already occupies */
            return false;
        }
    }

    public Boolean squareEmpty(int row, int col, GamePiece[][] board){
        if (board[row][col] == null){
            /* square is open */
            return true;
        }
        else {
            /* player tried landing on a square already occupied */
            return false;
        }
    }

    public Boolean moveTowardRiver(int destRow, int destCol){
        /* determines if the game piece is going directly towards the river - e.g. a vertical move */
        /* returns false if it's crossing the river or moving away from the river or diagonally */

        /* check for vertical move */
        if (destCol != this.column) return false;

        /* check for river crossing */
        if (((this.row > destRow) && (destRow >= GameBoard.riverRow)) ||
                ((this.row < destRow) && (destRow <= GameBoard.riverRow)))
            return true;
        else return false;
    }

    public Boolean pathClear(int destRow, int destCol, GamePiece[][] board){
        /* check to ensure no pieces along the path */
        /* WARNING - does not check that final destination is clear */
        int distCol = Math.abs(destCol - this.column);
        int distRow = Math.abs(destRow - this.row);

        if ((destRow == this.row) && (destCol == this.column)) return true;  /* destination same as origin */

        if ((destRow == this.row) /* horizontal move */
            || (destCol == this.column) /* vertical move */
            || (distCol == distRow) /* diagonal move */
        ) {
            int rowDir = (distRow == 0) ? 0 : (destRow - this.row)/distRow;  /* -1 for left dir, +1 for right dir, 0 for horizontal path */
            int colDir = (distCol == 0) ? 0 : (destCol - this.column)/distCol; /* -1 for down dir, +1 for up dir, 0 for vertical path */

            int x = this.column + colDir;
            int y = this.row + rowDir;
            while ((y != destRow) || (x != destCol)){
                /* Until we've reached the destination square, */
                /* travel along path and make sure all squares are NULL */
                if (!(squareEmpty(y, x, board))){
                    /* piece found in path */
                    return false;
                }

                x = x + colDir;
                y = y + rowDir;
            }
            return true;  /* if we get here, path was clear */
        }
        else {
            return false;  /* path is not a straight line */
        }
    }

    /* This routine executes one move for a specific piece other than Monkey.  Monkey can do a sequence of
    moves.  If there is another piece in the destination square of the move, then it is captured and removed from
    the board.
    NOTE - Monkey should use performMoveSeq()
     */
    public boolean performMove(int destRow, int destCol, GameBoard congoBoard) {
        if (ValidateMove(destRow, destCol, congoBoard.board)){
            if (!(squareEmpty(destRow, destCol, congoBoard.board))){
                congoBoard.capturePiece(congoBoard.board[destRow][destCol]);
            }

            congoBoard.movePiece(this.row, this.column, destRow, destCol);
            return true;
        }
        else return false;
    }

    public Boolean moveOneOrTwoStepStraightBackward (int destRow, int destCol, GamePiece[][] board){

        //check for one/two steps straight down
        int  distRow = Math.abs(destRow - this.row ) ;
        int distCol = Math.abs(destCol - this.column) ;

        if (( distRow == 2 && distCol == 0 ) || (distRow == 1 && distCol == 0 ))
            // if destination is empty not occupied by any pieces
            return (squareEmpty(destRow, destCol, board) && pathClear(destRow, destCol, board));
        else
            return false;
    }

    // move/capture side away
    public Boolean moveSideAwayForSuperPawn (int destRow , int destCol, GamePiece[][] board) {
        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        if (distRow == 0 && distCol == 1)
            return squareEmptyOrCapturable(destRow, destCol, board);

        else
            return false;
    }

    // move/capture one step forward
    public Boolean moveOneStepsStraightOrDiagonally(int destRow, int destCol, GamePiece[][] board){

        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        if ( (distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1))
            return squareEmptyOrCapturable(destRow, destCol, board);
        else
            return false;
    }


    public Boolean moveOneOrTwoStepsDiagonallyBackward (int destRow, int destCol, GamePiece[][] board) {

        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        //check for one/two steps diagonally down
        if ((distRow == 1 && distCol == 1 ) || (distRow == 2 && distCol == 2 ))
            // if destination is empty not occupied by any pieces
            return ( squareEmpty(destRow, destCol, board) && pathClear(destRow,destCol,board) );
        else
            return false;
    }
}
