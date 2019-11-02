package game.pieces;

import game.*;

public class LionPiece extends GamePiece {
    /* initial constructor*/
    public LionPiece() {
    }

    public LionPiece(int row, int col, int player) {
        super(row, col, player);
    }

    public String pieceIDString(){
        return (player == 1) ? "l" : "L";
    }

    // variable to store castle bound/border for each player
    int rowLowerBound , rowUpperBound;

    // helper function to check castle's ROW bound/border
    public boolean checkCastleRowBound(int destRow, int rowUpperBound, int rowLowerBound){
        if (destRow >= rowLowerBound && destRow <= rowUpperBound)
            return true;
        else
            return false;
    }

    // helper function to check castle's COLUMN bound/border
    public boolean checkCastleColumnBound(int destCol){
        if (destCol >= 2 && destCol <=4)
            return true;
        else
            return false;
    }

    // helper function to check if destination is in lion's own castle
    public boolean DestinationIsInOwnCastle(int destRow, int destCol)
    {
        if (this.player == 1) {
            rowLowerBound = 0;
            rowUpperBound= 2;
        }
        else {
            rowLowerBound = 4;
            rowUpperBound = 6;
        }

        if (checkCastleRowBound(destRow, rowUpperBound, rowLowerBound) && checkCastleColumnBound(destCol))
            return true;
        else
            return false;
    }

    // helper function to check if destination is in lion's opponent castle
    public boolean DestinationIsInOpponentCastle(int destRow, int destCol){
        if (this.player == 1)
        {
            rowLowerBound = 4;
            rowUpperBound = 6;
        }
        else{
            rowLowerBound = 0;
            rowUpperBound = 2;
        }

        if (checkCastleRowBound(destRow, rowUpperBound, rowLowerBound) && checkCastleColumnBound(destCol))
            return true;
        else
            return false;
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /*The Lion moves like a chess king, but may not leave his castle at his side of the river.
        In addition, lions can capture other lions if they `see' it, i.e.,
        if there is a vertical or diagonal line with no pieces between the two lions, the lion may jump to the other lion and capture it.*/

        /* check for out of bound moves */
        if (GameBoard.inBounds(destRow, destCol)) {

            // check if the destination located in own castle
            if (DestinationIsInOwnCastle(destRow, destCol)) {

                /*move one step to an empty square or capture if there is any opponent's piece  */
                if ((orthogonalMove(this.row, this.column, destRow, destCol) && manhattanDistance(this.row, this.column, destRow, destCol) == 1) || (diagonalMove(this.row, this.column, destRow, destCol) && manhattanDistance(this.row, this.column, destRow, destCol) == 2))

                    return squareEmptyOrCapturable(destRow, destCol, board);
            }

            // check if destination is in opponent's lion castle
            if (DestinationIsInOpponentCastle(destRow, destCol)){

                // if destination is in opponent's castle, it must be the other lion with a clear path between
                if ((board[destRow][destCol] instanceof LionPiece) && (pathClear(destRow, destCol, board)) && this.player != board[destRow][destCol].player)   /* The last condition never happens as every player has only one lion, but added to write extra tests */

                    return true;
                }else
                    return false;

            return false;
            }
        else
            return false;
        }
    }
