package Game;

public class GiraffePiece extends GamePiece {

    /* initial constructor*/
    public GiraffePiece() {
    }

    public GiraffePiece(int row, int col, int player) {
        super(row, col, player);
    }

    public String pieceIDString(){
        return (getPlayer() == 1) ? "g" : "G";
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {

        /* The Giraffe moves and captures by jumping to the second square in a straight or diagonal direction.
        A jump cannot be blocked by interposing pieces of either color.
        In addition it has the king's move, without the right to capture with it.*/

        /* check for out of bound moves */
        if (GameBoard.inBounds(destRow, destCol)) {

            /* check if Giraffe moved/jumped by two steps straight OR diagonal and it MUST capture*/
            if ((orthogonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 2)
                    || diagonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 4)

                return squareEmptyOrCapturable(destRow, destCol, board);

            /* check if Giraffe moved by one step straight OR diagonal in any direction and it CANNOT capture */
            if ((orthogonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1)
                    || diagonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 2) {

                return squareEmpty(destRow, destCol, board);
            }else
                return false;
        }
        else
            return false;
    }
}


