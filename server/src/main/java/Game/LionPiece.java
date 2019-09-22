package Game;

//import Game.GameBoard;

public class LionPiece extends GamePiece {
    /* initial constructor*/
    public LionPiece() {
    }

    public LionPiece(int row, int col, int player) {
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /*The Lion moves like a chess king, but may not leave his castle at his side of the river.
        In addition, lions can capture other lions if they `see' it, i.e.,
        if there is a vertical or diagonal line with no pieces between the two lions, the lion may jump to the other lion and capture it.*/

        /* check for out of bound moves */
        if (destRow > 6 || destRow < 0) {
            return false;
        }
        if (destCol > 6 || destCol < 0) {
            return false;
        }

         //check for out of castle for lion in down side
        if ((destRow >= 0 && destRow <= 2 && destCol >= 2 && destCol <= 4)){

            /*Check for chess king move, to any arbitrary direction just by one step */

            /* calculate distance to destination */
            int distrow = Math.abs(destRow - this.row);
            int distcol = Math.abs(destCol - this.column);

            /*move one step to an empty square or capture if there is any opponent's piece  */
            if ( distrow <= 1 && distcol <=1 ){
                return squareEmptyOrCapturable(destRow, destCol, board);
            }
            else if (board[destRow][destCol] instanceof LionPiece){
                if (pathClear(destRow, destCol, board))
                {
                    System.out.println("Game is Over");
                    return true;
                }
                else
                    return false;
            }
        }

        if ((destRow >= 4 && destRow <= 6 && destCol >= 2 && destCol <= 4)){
            int distrow = Math.abs(destRow - this.row);
            int distcol = Math.abs(destCol - this.column);

            if ( distrow <= 1 && distcol <=1 ){
                return squareEmptyOrCapturable(destRow, destCol, board);
            }
            if (board[destRow][destCol] instanceof LionPiece){
                if (pathClear(destRow, destCol, board))
                {
                    System.out.println("Game is Over");
                    return true;
                }
                else
                    return false;
            }
            else
                return false;
        }

        else
            return false;
    }
}