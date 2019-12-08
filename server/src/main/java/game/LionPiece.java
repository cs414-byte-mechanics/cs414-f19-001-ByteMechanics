package Game;

public class LionPiece extends GamePiece {
    //Store castle bounds
    private int rowLowerBound;
    private int rowUpperBound;
    
    /* initial constructor*/
    public LionPiece() {
    }

    public LionPiece(int row, int col, int player) {
        super(row, col, player);
    }

    public String pieceIDString(){
        return (getPlayer() == 1) ? "l" : "L";
    }

    // helper function to check castle's ROW bound/border
    public boolean checkCastleRowBound(int destRow, int rowUpperBound, int rowLowerBound){
        return destRow >= rowLowerBound && destRow <= rowUpperBound;
    }

    // helper function to check castle's COLUMN bound/border
    public boolean checkCastleColumnBound(int destCol){
        return destCol >= 2 && destCol <= 4;
    }

    // helper function to check if destination is in lion's own castle
    public boolean DestinationIsInOwnCastle(int destRow, int destCol)
    {
        if (getPlayer() == 1) {
            rowLowerBound = 0;
            rowUpperBound= 2;
        }
        else {
            rowLowerBound = 4;
            rowUpperBound = 6;
        }

        return checkCastleRowBound(destRow, rowUpperBound, rowLowerBound) && checkCastleColumnBound(destCol);
    }

    // helper function to check if destination is in lion's opponent castle
    public boolean DestinationIsInOpponentCastle(int destRow, int destCol){
        if (getPlayer() == 1)
        {
            rowLowerBound = 4;
            rowUpperBound = 6;
        }
        else{
            rowLowerBound = 0;
            rowUpperBound = 2;
        }

        return checkCastleRowBound(destRow, rowUpperBound, rowLowerBound) && checkCastleColumnBound(destCol);
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
                if ((orthogonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1) || (diagonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol) == 2))

                    return squareEmptyOrCapturable(destRow, destCol, board);
            }

            // check if destination is in opponent's lion castle
            if (DestinationIsInOpponentCastle(destRow, destCol)){

                // if destination is in opponent's castle, it must be the other lion with a clear path between
                /* The last condition never happens as every player has only one lion, but added to write extra tests */
                return (board[destRow][destCol] instanceof LionPiece) && (pathClear(destRow, destCol, board)) && getPlayer() != board[destRow][destCol].getPlayer();
                }else
                    return false;

        }
        else
            return false;
        }
    }
