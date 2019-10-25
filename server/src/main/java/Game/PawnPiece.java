package Game;

import Game.GameBoard;

import java.util.concurrent.SynchronousQueue;

public class PawnPiece extends GamePiece {

    // every pawn can be promoted as a superPawn / can cross river 
    boolean superPawn  ;

    /* initial constructor*/
    public PawnPiece(){
    }

    public PawnPiece(int row, int col, int player){
//        super(row, col, player);
        this.row = row;
        this.column = col;
        this.player = player;
        this.superPawn = false;
//        this.crossedRiver = false;
    }

    public String pieceIDString(){
        return (player == 1) ? "p" : "P";
    }

    /*helper routine to check sideAway move*/
    public boolean sideMove(int currRow , int destRow){
        if (destRow - currRow  == 0 )
            return true;
        else
            return false;
    }

    /* helper routine to identify if move is forward or backward for a pawn*/
    public boolean forwardMove(int fromRow, int toRow)
    {
        int dir = toRow - fromRow;
        if (this.player == 1 && dir > 0)
            return true;

        if (this.player == 2 && dir < 0)
            return true;

        return false;
    }

    public boolean pastRiver(int currRow){
        if (this.player == 1 && currRow > GameBoard.RIVER_ROW)
            return true;

        if(this.player == 2 && currRow < GameBoard.RIVER_ROW)
            return true;

        return false;
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
        /* A pawn moves and captures one step straight or diagonally forward. When past the river, it can also move (but not capture) one or two steps straight backward (without jumping).*/

        if (GameBoard.validBoardLocation(destRow, destCol))
        {
            /* pawn can move one step straight/diagonally forward- it can also capture*/
            if (forwardMove(this.row, destRow) && (manhattanDistance(this.row, this.column, destRow, destCol) == 1 || ( diagonalMove(this.row, this.column, destRow, destCol) && manhattanDistance(this.row, this.column, destRow, destCol)==2 )))
                return squareEmptyOrCapturable(destRow, destCol, board);

            /* when pawn past the river, it can move (not capture) one or two steps straight backward*/
            if (pastRiver(this.row) && !forwardMove(this.row, destRow) && orthogonalMove(this.row, this.column, destRow, destCol) && pathClear(destRow, destCol, board) && !sideMove(this.row , destRow))
                if (manhattanDistance(this.row, this.column, destRow, destCol) == 1 || manhattanDistance(this.row, this.column, destRow, destCol) == 2)

                    return squareEmpty(destRow, destCol, board);

            /* superPawn in addition can move and capture one step straight sideways */
            if (superPawn == true && orthogonalMove(this.row, this.column, destRow, destCol)) {
                if (manhattanDistance(this.row, this.column, destRow, destCol) == 1)

                    return squareEmptyOrCapturable(destRow, destCol, board);
            }

            /* superPawn moves (but not capture) one or two steps straight or diagonally backward (without jumping). */
            if(superPawn ==true && pathClear(destRow, destCol, board) && !forwardMove(this.row, destRow))
                if(diagonalMove(this.row, this.column, destRow, destCol) && (manhattanDistance(this.row, this.column, destRow, destCol)==2 || manhattanDistance(this.row, this.column, destRow, destCol) == 4 ))

                    return squareEmpty(destRow, destCol, board);
        }
        else
            return false;

        return false;
    }
}

