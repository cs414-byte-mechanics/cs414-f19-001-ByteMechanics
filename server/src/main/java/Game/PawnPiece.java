package Game;

import Game.GameBoard;

import java.util.concurrent.SynchronousQueue;

public class PawnPiece extends GamePiece {

    // every pawn can be promoted as a superPawn / can cross river 
    boolean superPawn , crossedRiver ;

    /* initial constructor*/
    public PawnPiece(){
    }

    public PawnPiece(int row, int col, int player){
//        super(row, col, player);
        this.row = row;
        this.column = col;
        this.player = player;
        this.superPawn = false;
        this.crossedRiver = false;
    }

    public String pieceIDString(){
        return (player == 1) ? "p" : "P";
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
            //System.out.println("****"+superPawn+this.row+this.column);

            if (superPawn == true) {
                System.out.println("I am SUPER pawn1 !!!!!");
                // like a normal pawn can move/capture one step straight or diagonally forward
                if (destRow > this.row) {

                    return moveOneStepsStraightOrDiagonally(destRow, destCol, board);
                }

                // like a normal pawn, it can also move (but not capture) one or two steps straight backward (without jumping)
                if (destRow < this.row){
                    return ( (moveOneOrTwoStepStraightBackward(destRow, destCol, board)) || (moveOneOrTwoStepsDiagonallyBackward(destRow, destCol, board)) ) ;
                }

                // as a superPawn s/he can move/capture one square sideAway
                if (destRow == this.row) {
//                    System.out.println("can move side1");
                    return moveSideAwayForSuperPawn(destRow, destCol, board);
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
                if (destRow > this.row && destRow > GameBoard.RIVER_ROW ) {

                    // calculate distance to destination
                    int distRow = destRow - this.row;
                    int distCol = destCol - this.column;

                    // on the other side of river each pawn can still move one step straight or diagonally forward and capture !
                    if ((distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1))
                        return squareEmptyOrCapturable(destRow, destCol, board);
                }

                // after landing across the river , set flag to true
                if (this.row > GameBoard.RIVER_ROW ){
                    crossedRiver = true;
                    //System.out.println("Crossed the river");
                }

                // check if landing across the river promoted him as a super pawn
                if (this.row == 6){
                    superPawn = true;
                    System.out.println("Promoted as super Pawn1");
                        }

                //if s/he crossed the river he can move one/steps straight backward -- NO JUMP and NO CAPTURE
                if (crossedRiver = true && destRow < this.row ) {

                    // he can move but not jump backward by one or two steps straight - no capture
                    return (moveOneOrTwoStepStraightBackward(destRow, destCol, board)); // changed
                }

                // in backward, maybe he landed in river or down side of river , so crossedRiver flag is false as he is not yet super pawn
                if (this.row <= GameBoard.RIVER_ROW && this.row >= 0)
                    crossedRiver = false;
            }
            // just added - in case if player is not either pawn or super pawn
//            return false;
        }

        if (this.player == 2) {
            // if s/he is a super pawn ???
//            System.out.println("SuperPawn flag"+superPawn);

            if (superPawn == true) {
                System.out.println("I am SUPER pawn2!!");

                // like a normal pawn, super pawn can move/capture one step straight or diagonally forward
                if (destRow < this.row) {
                    return moveOneStepsStraightOrDiagonally(destRow, destCol, board);
                }

                // like a normal pawn, it can also move (but not capture) one or two steps straight/diagonally backward (without jumping)
                if (destRow > this.row){
                    return ( moveOneOrTwoStepStraightBackward(destRow, destCol, board) || moveOneOrTwoStepsDiagonallyBackward(destRow, destCol, board) );
                }

                // as a superPawn s/he can move/capture one square sideAway
                if (destRow == this.row) {
                    System.out.println("can move side");
                    return moveSideAwayForSuperPawn(destRow, destCol, board);
                }
            }

            // if s/he is not super pawn
            if (superPawn == false) {

                // They can move toward the river ( don't cross the river) by one step straight and diagonally and capture-

                // if destination does not cross the river - row 6,5,4,3
                if (destRow < this.row && destRow >= 3 && destRow <= 6) {
                    return moveOneStepsStraightOrDiagonally(destRow, destCol, board);
                }

                // if destination is cross the river
                if (destRow < this.row && destRow < GameBoard.RIVER_ROW ) {
                    return moveOneStepsStraightOrDiagonally(destRow, destCol, board);
                }

                // after landing across the river , set flag to true
                if (this.row < GameBoard.RIVER_ROW ){
                    crossedRiver = true;
                    //System.out.println("Crossed the river");
                }

                // check if landing across the river promoted him as a super pawn
                if (this.row == 0){
                    superPawn = true;
                    System.out.println("Promoted as super Pawn2");
                }

                //if s/he crossed the river he can move one/steps straight backward -- NO JUMP and NO CAPTURE
                if (crossedRiver = true && destRow > this.row ) {
                    // he can move but not jump backward by one or two steps straight - no capture
                    return ( moveOneOrTwoStepStraightBackward(destRow, destCol, board) );
                }

                // in backward, maybe he landed in river or down side of river , so crossedRiver flag is false as he is not yet super pawn
                if (this.row >= GameBoard.RIVER_ROW && this.row <= 6)
                    crossedRiver = false;
            }
        }

        return false;
    }
}

