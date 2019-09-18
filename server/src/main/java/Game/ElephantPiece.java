package Game;

public class ElephantPiece extends GamePiece {

    /* initial constructor*/
    public ElephantPiece(){
    }

    public ElephantPiece(int row, int col, int player){
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board)
    {
        /* Elephant  can move to the first and second square in a straight direction.
           The move to the second square is a jump and cannot be blocked by interposing pieces of either color. */

        /* check for out of bound moves */
        if (destRow > 6 || destRow < 0){
            return false;
        }
        if (destCol > 6 || destCol < 0){
            return  false;
        }

        /* check if elephant moved one step straight forward OR two steps straight forward*/
        if ((destRow - this.row == 1 && destCol - this.column == 0 ) || (destRow - this.row == 2 && destCol - this.column == 0 )) {
            return true;
        }

        /* Destination square must be empty or contain opponents piece.  If it contains
           opponents piece then it will be captured.*/
        int activePlayer = this.player;
        if ((board[destRow][destCol] == null) || (board[destRow][destCol].player != activePlayer)) {
            return true;
        }
        else {
            return false;
        }

    }

}
