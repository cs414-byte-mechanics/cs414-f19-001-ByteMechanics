package Game;

public class ElephantPiece extends GamePiece {

    /* initial constructor*/
    public ElephantPiece(){
    }

    public ElephantPiece(int row, int col, int player){
        super(row, col, player);
    }

    public String pieceIDString(){
        return (player == 1) ? "e" : "E";
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board)
    {
        /* Elephant  can move to the first and second square in a orthogonal direction.
           The move to the second square is a jump and cannot be blocked by interposing pieces of either color. */

        /* check for out of bound moves */
        if (destRow > 6 || destRow < 0){
            return false;
        }
        if (destCol > 6 || destCol < 0){
            return  false;
        }

        /* check if elephant moved one OR two steps straight - can moe and can capture*/
        if (orthogonalMove(this.row, this.column, destRow, destCol) &&
            (manhattanDistance(this.row, this.column, destRow, destCol) ==1 || manhattanDistance(this.row, this.column, destRow, destCol) ==2))

            return squareEmptyOrCapturable(destRow, destCol, board);
        else
            return false;
    }
}
