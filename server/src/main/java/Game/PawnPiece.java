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
        if (destRow < 0 || destRow > 6)
            return false;

        if (destCol < 0 || destCol > 6)
            return false;

        // for pawns belongs to player1
        if (this.player == 1) {

            // if s/he is a super pawn ???
            if (superPawn == true) {

                // like a normal pawn can move/capture one step straight or diagonally forward
                if (destRow > this.row) {

                    int distRow = Math.abs(destRow - this.row);
                    int distCol = Math.abs(destCol - this.column);

                    return moveOneStepsStraightOrDiagonally(distRow, distCol, board);
                }

                // like a normal pawn, it can also move (but not capture) one or two steps straight backward (without jumping)
                if (destRow < this.row){

                    int distRow = Math.abs(destRow - this.row);
                    int distCol = Math.abs(destCol - this.column);

                    return moveOneOrTwoStepStraightBackward(distRow, distCol, board);
                }

                // as a superPawn s/he can move/capture one square sideAway
                if (destRow == this.row) {

                    int distRow = Math.abs(destRow - this.row);
                    int distCol = Math.abs(destCol - this.column);

                    return moveSideAwayForSuperPawn(distRow, distCol, board);
                }

                //As a super pawn, it can move (but not capture) one or two steps straight or diagonally backward (without jumping)
                if (destRow < this.row) {

                    int distRow = Math.abs(destRow - this.row);
                    int distCol = Math.abs(destCol - this.column);

                    return ( pathClear(destRow, destCol, board) && moveOneOrTwoStepsDiagonallyBackward (distRow, distCol, board));
                }
            }

            // if s/he is not super pawn
            if (superPawn == false) {

                // They can move toward the river ( don't cross the river) by one step straight and diagonally and capture-

                // if destination does not cross the river - row 0,1,2,3
                if (destRow > this.row && destRow >= 0 && destRow <= 3) {

                    // calculate distance to destination
                    int distRow = destRow - this.row;
                    int distCol = destCol - this.column;

                    // move and capture one step forward or diagonally
                    if ((distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1)) {
                        return squareEmptyOrCapturable(destRow, destCol, board);
                    }
                }

                // if destination cross the river
                if (destRow > this.row && destRow > GameBoard.riverRow ) {

                    // because it passed river!
//                    crossedRiver = true;

                    // calculate distance to destination
                    int distRow = destRow - this.row;
                    int distCol = destCol - this.column;

                    // on the other side of river each pawn can still move one step straight or diagonally forward and capture !
                    if ((distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1))
                        return squareEmptyOrCapturable(destRow, destCol, board);
                }

                // after landing across the river , set flag to true
                if (this.row > GameBoard.riverRow )
                    crossedRiver = true;

                // check if landing across the river promoted him as a super pawn
                if (this.row == 6)
                    superPawn = true;

                //if s/he crossed the river he can move one/steps straight backward -- NO JUMP and NO CAPTURE
                if (crossedRiver = true && destRow < this.row ) {
                    // calculate distance to destination
                    int distRow = Math.abs (destRow - this.row);
                    int distCol = Math.abs (destCol - this.column);

                    // he can move/not jump backward by one or two steps straight - no capture
                    return (pathClear(destRow, destCol, board) && moveOneOrTwoStepStraightBackward(distRow, distCol, board));
                }

                // in backward, maybe he landed in river or down side of river , so crossedRiver flag is false as he is not yet super pawn
                if (this.row <= GameBoard.riverRow && this.row >= 0)
                    crossedRiver = false;
            }

        }
        return false; // need to be deleted!!
    }
}

// Add clear path for backward
// add / delete necessary return at the end