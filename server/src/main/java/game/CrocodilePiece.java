package game;

public class CrocodilePiece extends GamePiece {
    public CrocodilePiece(){
    }

    public CrocodilePiece(int row, int col, int player) {
        super(row, col, player);
    }

    public String pieceIDString(){
        return (player == 1) ? "c" : "C";
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /* A crocodile has three different move types.
            1) At any time it can move similar to a King piece in chess
                - one step in any direction with capability to capture opponent's piece.
            2) If direction of travel is towards the river then it can move like a rook -
                - Any number of squares up to and including the square in the river.
                - Cannot jump over pieces but can capture a piece along the path to the river.
            3) Once the crocodile is in the river -
                - Crocodile can move & capture like a rook along the length of the river.
            */

        /* check for out of bounds moves */
        if (GameBoard.inBounds(destRow, destCol)) {

            int distCol = Math.abs(destCol - this.column);
            int distRow = Math.abs(destRow - this.row);
            /* Crocodile moves 1 square in any direction.  */
            if ((distCol <= 1) && (distRow <= 1)) {
            /* Crocodile is moving a single square in any direction.
            Destination square must be empty or contain opponents piece.  If it contains
            opponents piece then it will be captured. */
                return squareEmptyOrCapturable(destRow, destCol, board);
            }

            /* Check if move is along the river */
            if (inRiver() & inRiver(destRow)) {
                /* crocodile and destination location are in river */
                /* are we going upriver (+1) or downriver (-1) */
                int riverDir = (this.column < destCol) ? 1 : -1;

                /* check for blocking pieces along the river */
                if (!(pathClear(destRow, destCol, board)))
                    return false;

                /* check that destination square is open or contains opponent's piece */
                return squareEmptyOrCapturable(destRow, destCol, board);
            }

            /* Check if this is a longer move DIRECTLY toward the river */
            if (moveTowardRiver(destRow, destCol)) {
                /* check for blocking pieces along the river */
                if (!(pathClear(destRow, destCol, board)))
                    return false;
            /* We've determined that path is clear, so Destination square must be empty or contain opponents piece.
            If it contains opponents piece then it will be captured. */
                return squareEmptyOrCapturable(destRow, destCol, board);
            } else
                return false;
        }
        else
            return false;
    }
}
