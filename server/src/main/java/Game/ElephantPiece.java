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

        int distRow = Math.abs(destRow - this.row );
        int distCol = Math.abs(destCol - this.column);

        /* check if elephant moved one OR two steps straight - can moe and can capture*/
        if ( (distRow== 1 && distCol == 0 ) || (distRow == 2 && distCol == 0 ) || (distRow== 0 && distCol == 1) || (distRow== 0 && distCol == 2)) {
            return squareEmptyOrCapturable(destRow, destCol, board);
        }
        else
            return false;

    }
}
