package Game;

import Game.GameBoard;

public class GamePiece {
    public int row;
    public int column;
    public int player;  /* set to 1 or 2 to indicate which player owns the piece */

    public GamePiece(){
    }

    public GamePiece(int r, int c, int p){
        row = r;
        column = c;
        player = p;
    }

    public Boolean inRiver(){
        /* determines if playing piece is currently in the river */
        if (this.row == GameBoard.riverRow)
            return true;
        else
            return false;
    }

    public Boolean inRiver(int r){
        /* determines if specific location is in the river */
        if (r == GameBoard.riverRow)
            return true;
        else
            return false;
    }

    public Boolean squareEmptyOrCapturable(int row, int col, GamePiece[][] board){
        if (board[row][col] == null || board[row][col].player != this.player){
            /* square is open or contains opponent's piece */
            return true;
        }
        else {
            /* player tried landing on a square s/he already occupies */
            return false;
        }
    }

    public Boolean squareEmpty(int row, int col, GamePiece[][] board){
        if (board[row][col] == null){
            /* square is open */
            return true;
        }
        else {
            /* player tried landing on a square already occupied */
            return false;
        }
    }

    public Boolean moveTowardRiver(int destRow, int destCol){
        /* determines if the game piece is going directly towards the river - e.g. a vertical move */
        /* returns false if it's crossing the river or moving away from the river or diagonally */

        /* check for vertical move */
        if (destCol != this.column) return false;

        /* check for river crossing */
        if (((this.row > destRow) && (destRow >= GameBoard.riverRow)) ||
                ((this.row < destRow) && (destRow <= GameBoard.riverRow)))
            return true;
        else return false;
    }

    public Boolean pathClear(int destRow, int destCol, GamePiece[][] board){
        /* check to ensure no pieces along the path */
        /* WARNING - does not check that final destination is clear */
        int distCol = Math.abs(destCol - this.column);
        int distRow = Math.abs(destRow - this.row);

        if ((destRow == this.row) && (destCol == this.column)) return true;  /* destination same as origin */

        if ((destRow == this.row) /* horizontal move */
            || (destCol == this.column) /* vertical move */
            || (distCol == distRow) /* diagonal move */
        ) {
            int rowDir = (destRow - this.row)/distRow;  /* -1 for left dir, +1 for right dir, 0 for horizontal path */
            int colDir = (destCol - this.column)/distCol; /* -1 for down dir, +1 for up dir, 0 for vertical path */
            int x = this.column + colDir;
            int y = this.row + rowDir;
            while ((y != destRow) && (x != destCol)){
                /* travel along path and make sure all squares are NULL */
                if (board[x][y] != null){
                    /* piece found in path */
                    return false;
                }

                x = x + colDir;
                y = y + rowDir;
            }
            return true;  /* if we get here, path was clear */
        }
        else return false;  /* path is not a straight line */
    }


}

