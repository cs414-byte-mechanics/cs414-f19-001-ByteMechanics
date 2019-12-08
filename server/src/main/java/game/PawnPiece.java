package Game;

public class PawnPiece extends GamePiece {

    // every pawn can be promoted as a superPawn / can cross river 
    private boolean superPawn ;

    /* initial constructor*/
    public PawnPiece(){ }

    public PawnPiece(int row, int col, int player, char pieceType){
        super(row, col, player);
        superPawn = ((pieceType == 'p') || (pieceType == 'P')) ? false : true;
    }

    public String pieceIDString(){
        if (this.superPawn){
            return (getPlayer() == 1) ? "s" : "S";}
        else
            return (getPlayer() == 1) ? "p" : "P";
    }
    
    public void setSuperPawn(boolean sp){
        superPawn = sp;
    }

    /*helper routine to check sideAway move*/
    public boolean sideMove(int currRow , int destRow){
        return destRow - currRow == 0;
    }

    /* helper routine to identify if move is forward or backward for a pawn*/
    public boolean forwardMove(int fromRow, int toRow)
    {
        int dir = toRow - fromRow;
        if (getPlayer() == 1 && dir > 0)
            return true;

        return getPlayer() == 2 && dir < 0;
    }

    public boolean pastRiver(int currRow){
        if (getPlayer() == 1 && currRow > GameBoard.RIVER_ROW)
            return true;

        return getPlayer() == 2 && currRow < GameBoard.RIVER_ROW;
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /* A pawn moves and captures one step straight or diagonally forward. When past the river, it can also move (but not capture) one or two steps straight backward (without jumping).*/

        if (GameBoard.inBounds(destRow, destCol))
        {
            /* pawn can move one step straight/diagonally forward- it can also capture*/
            if (forwardMove(getRow(), destRow) && (manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1 || ( diagonalMove(getRow(), getColumn(), destRow, destCol) && manhattanDistance(getRow(), getColumn(), destRow, destCol)==2 )))
                return squareEmptyOrCapturable(destRow, destCol, board);

            /* when pawn past the river, it can move (not capture) one or two steps straight backward*/
            if (pastRiver(getRow()) && !forwardMove(getRow(), destRow) && orthogonalMove(getRow(), getColumn(), destRow, destCol) && pathClear(destRow, destCol, board) && !sideMove(getRow() , destRow))
                if (manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1 || manhattanDistance(getRow(), getColumn(), destRow, destCol) == 2)

                    return squareEmpty(destRow, destCol, board);

            /* superPawn in addition can move and capture one step straight sideways */
            if (superPawn == true && orthogonalMove(getRow(), getColumn(), destRow, destCol)) {
                if (manhattanDistance(getRow(), getColumn(), destRow, destCol) == 1)

                    return squareEmptyOrCapturable(destRow, destCol, board);
            }

            /* superPawn moves (but not capture) one or two steps straight or diagonally backward (without jumping). */
            if(superPawn ==true && pathClear(destRow, destCol, board) && !forwardMove(getRow(), destRow))
                if(diagonalMove(getRow(), getColumn(), destRow, destCol) && (manhattanDistance(getRow(), getColumn(), destRow, destCol)==2 || manhattanDistance(getRow(), getColumn(), destRow, destCol) == 4 ))

                    return squareEmpty(destRow, destCol, board);
        }
        else
            return false;

        return false;
    }
}

