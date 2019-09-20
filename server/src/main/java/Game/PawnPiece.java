package Game;

import Game.GameBoard;

public class PawnPiece extends GamePiece {
    // every pawn can be promoted as a superPawn
    boolean superPawn = false;

    // need to check if he passed the river or no
    boolean crossedRiver = false;

    /* initial constructor*/
    public PawnPiece(){
    }

    public PawnPiece(int row, int col, int player){
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /* A pawn moves and captures one step straight or diagonally forward.
        When past the river, it can also move (but not capture) one or two steps straight backward (without jumping).*/

        // check for out of move bounds
        if ( destRow < 0 || destRow > 6 )
            return false;

        if (destCol < 0 ||  destCol > 6 )
            return false;

        // for downside pawns
        if (this.player == 1 ) {

            // check he crossed the river already or no ?
            if (this.row > 3)
                crossedRiver = true;

            // check s/he promoted as a superPawn?
            if (this.row == 6)
                superPawn = true;

            // They can move toward the river ( don't cross the river) by one/two steps straight and diagonally and capture-
            if (destRow > this.row && destRow >= 0 && destRow <= 3) {

                // calculate distance to destination
                int distRow = destRow - this.row;
                int distCol = destCol - this.column;

                // move and capture one step forward or diagonally
                if ((distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1)) {
                    return squareEmptyOrCapturable(destRow, destCol, board);
                }
            }

            // check to see if pawns pass the river and land in the opponent's area
            if (destRow > this.row && destRow > GameBoard.riverRow) {

                // because it passed river!
                crossedRiver = true;

                // calculate distance to destination
                int distRow = destRow - this.row;
                int distCol = destCol - this.column;

                // on the other side of river each pawn can still move one step straight or diagonally forward and capture !
                if ((distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1))
                    return squareEmptyOrCapturable(destRow, destCol, board);

                // check s/he promoted as a superPawn
//                if (this.row == 6)
//                    superPawn = true;
            }
            // S/he even can move backward one/two steps when he already passed the river - NOT AUTHORIZED TO CAPTURE
            if (destRow < this.row && crossedRiver ) {

                // calculate distance to destination
                int distRow = destRow - this.row;
                int distCol = destCol - this.column;

                // he can move backward by one or two steps
                return moveOneOrTwoStepBackwardDown(distRow, distCol, board);
            }

            // check s/he promoted as a superPawn
            if (superPawn == true ){

                // calculate distance to destination
                int distRow = Math.abs(destRow - this.row);
                int distCol = Math.abs(destCol - this.column);

                // as a superPawn s/he can move/capture one square sideAway
                if (distRow == 0 && distCol == 1)
                    return squareEmptyOrCapturable(destRow, destCol, board);

                if (distRow==2 && distCol ==0 || distRow==2 && distCol ==2)
                    return squareEmpty(destRow, destCol, board);

            }
        }

        return true; // need to be deleted!
    }
}
