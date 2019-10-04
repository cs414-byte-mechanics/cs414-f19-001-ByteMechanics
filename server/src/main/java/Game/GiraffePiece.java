package Game;

public class GiraffePiece extends GamePiece {

    /* initial constructor*/
    public GiraffePiece() {
    }

    public GiraffePiece(int row, int col, int player) {
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board){

        /* The Giraffe moves and captures by jumping to the second square in a straight or diagonal direction.
        A jump cannot be blocked by interposing pieces of either color.
        In addition it has the king's move, without the right to capture with it.*/

        /* check for out of bound moves */
        if (destRow > 6 || destRow < 0) {
            return false;
        }
        if (destCol > 6 || destCol < 0) {
            return false;
        }

        int distRow = destRow - this.row;
        int distCol = destCol - this.column;

        /* check if Giraffe moved/jumped by two steps straight OR diagonal forward and it MUST capture*/
//        if ( (distRow == 2 && distCol == 0) || (distRow == 2 && distCol == 2) )
//            return squareEmptyOrCapturable(destRow, destCol, board);

        if ( (Math.abs(distRow) == 0 && Math.abs(distCol) ==2)  || (Math.abs(distRow) == 2 && Math.abs(distCol) ==0) || (Math.abs(distRow) == 2 && Math.abs(distCol) ==2) )
            return squareEmptyOrCapturable(destRow, destCol, board);

        /* check if Giraffe moved by one step straight OR diagonal in any direction and it CANNOT capture */
        if (Math.abs(distRow ) <= 1 && Math.abs(distCol) <= 1) {
            return squareEmpty(destRow, destCol, board);
        }
        else
            return false;
    }
}
