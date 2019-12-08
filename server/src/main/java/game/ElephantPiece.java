package Game;

public class ElephantPiece extends GamePiece {

    /* initial constructor*/
    public ElephantPiece(){
    }

    public ElephantPiece(int row, int col, int player){
        super(row, col, player);
    }

    public String pieceIDString(){
        return (getPlayer() == 1) ? "e" : "E";
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /* Elephant  can move to the first and second square in a orthogonal direction.
           The move to the second square is a jump and cannot be blocked by interposing pieces of either color. */

        /* check for out of bound moves */
        if (GameBoard.inBounds(destRow, destCol)) {

            /* check if elephant moved one OR two steps straight - can moe and can capture*/
            if (orthogonalMove(getRow(), getColumn(), destRow, destCol) &&
                    (manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1 || manhattanDistance(getRow(), getColumn(), destRow, destCol) == 2))

                return squareEmptyOrCapturable(destRow, destCol, board);
            else
                return false;
        }
        else
            return false;
    }
}
