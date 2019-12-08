package Game;

import java.util.ArrayList;

public abstract class GamePiece {
    private int row;
    private int column;
    private int player;  /* set to 1 or 2 to indicate which player owns the piece */
    public String pieceID;

    public GamePiece(){}

    public GamePiece(int r, int c, int p){
        row = r;
        column = c;
        player = p;
    }
    
    public GamePiece(int r, int c, String pieceID){
        row = r;
        column = c;
        this.pieceID = pieceID;
    }

    /**
    Returns information about the state of the piece including row, column, pieceID, and player
    */
    public String toString() {
        return ("pieceID: "+pieceIDString()+"  row: "+row+"  column: "+column+"  player: "
                +player+"\n");
    }

    public int getRow(){
        return row;
    }
    
    public void setRow(int r){
        row = r;
    }

    public int getColumn(){
        return column;
    }
    
    public void setColumn(int c){
        column = c;
    }

    public int getPlayer(){
        return player;
    }

    public abstract String pieceIDString();
    public abstract boolean ValidateMove(int destRow, int destCol, GamePiece[][] board);

    /**
    This method allows any piece other than monkey to handle recieving it's move specifications
        either using an array or individual integer values to indicate the square locations it is traversing.
        
        Since all non-monkey pieces can only move one square at a time, the array can never contain more than
        one square location.
    */
    public boolean ValidateMove(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GamePiece[][] board){
        
        if (destRow.size()>1 || destCol.size()>1)
            return false;
        else
            return ValidateMove(destRow.get(0), destCol.get(0), board);
    }

    protected GamePiece jumpCapturesPiece(int fromRow, int fromCol, int destRow, int destCol, GamePiece[][] board){
        /* Returns the object of a Gamepiece that was captured by jumping over it.
        Monkey is the only piece that captures by jumping.  All other pieces capture by landing on the square.
        So, this method returns NULL for any pieces that don't capture with a jump move.
         */
        return null;
    }

    /**
    Checks if the move from (fromRow, fromCol) to (toRow, toCol) is an orthogonal move. A move in any direction except diagonal is considered orthogonal
    */
    public boolean orthogonalMove(int fromRow, int fromCol, int toRow, int toCol){
        return fromRow == toRow || fromCol == toCol;
    }

    /**
    Checks if the move from (fromRow, fromCol) to (toRow, toCol) is a diagonal move
    */
    public boolean diagonalMove(int fromRow, int fromCol, int toRow, int toCol){
        return Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol);
    }

    /**
    Returns the manhattan distance from (fromRow, fromCol) to (toRow, toCol). Manhattan distance is the distance between two positions measured along axes at a right angle
    */
    public static int manhattanDistance(int fromRow, int fromCol, int toRow, int toCol){
        /* Returns the manhattan distance associated with a moves coordinates */
        return (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol));
    }

    public boolean jumpLinear(int fromRow, int fromCol, int toRow, int toCol){
        if (orthogonalMove(fromRow, fromCol, toRow, toCol) &&
                manhattanDistance(fromRow, fromCol, toRow, toCol) == 2)
            return true;
        return diagonalMove(fromRow, fromCol, toRow, toCol) &&
                manhattanDistance(fromRow, fromCol, toRow, toCol) == 4;
    }

    public boolean validMove1SquareAnyDirection(int fromRow, int fromCol, int toRow, int toCol){
        int manhattanDist = manhattanDistance(fromRow,fromCol,toRow,toCol);
        return (orthogonalMove(fromRow, fromCol, toRow, toCol) && manhattanDist == 1)
                || (diagonalMove(fromRow, fromCol, toRow, toCol) && manhattanDist == 2);
    }

    public Boolean inRiver(){
        /* determines if playing piece is currently in the river */
        return inRiver(getRow());
    }

    public Boolean inRiver(int r){
        return r == GameBoard.RIVER_ROW;
    }

    public Boolean squareEmptyOrCapturable(int row, int col, GamePiece[][] board){
        return squareEmpty(row, col, board) || board[row][col].player != this.player;
    }

    public Boolean squareEmpty(int row, int col, GamePiece[][] board){
        return board[row][col] == null;
    }

    /**
    determines if the game piece is going directly towards the river - e.g. a vertical move; returns false if it's crossing the river or moving away from the river or diagonally
    */
    public Boolean moveTowardRiver(int destRow, int destCol){
        /* check for vertical move */
        if (destCol != getColumn()) return false;

        /* check for river crossing */
        return ((getRow() > destRow) && (destRow >= GameBoard.RIVER_ROW)) ||
                ((getRow() < destRow) && (destRow <= GameBoard.RIVER_ROW));
    }

    public int direction(int from, int to){
        /* returns -1 if direction is left/down, 0 for vertical/horizontal, 1 for right/up */
        int dir = to > from ? +1 : to < from ? -1 : 0;
        return dir;
    }

    public Boolean pathClear(int destRow, int destCol, GamePiece[][] board){
        /* check to ensure no pieces along the path */
        /* WARNING - does not check that final destination is clear */

        if ((destRow == getRow()) && (destCol == getColumn())) return true;  /* destination same as origin */

        if (orthogonalMove(getRow(), getColumn(), destRow, destCol)
            || diagonalMove(getRow(), getColumn(), destRow, destCol))  {

            int rowDir = direction(getRow(),destRow);  /* -1 for left dir, +1 for right dir, 0 for horizontal */
            int colDir = direction(getColumn(),destCol);  /* -1 for down dir, +1 for up dir, 0 for vertical */

            int x = getColumn() + colDir;
            int y = getRow() + rowDir;
            while ((y != destRow) || (x != destCol)){
                /* Until we've reached the destination square, */
                /* travel along path and make sure all squares are NULL */
                if (!(squareEmpty(y, x, board))){
                    /* piece found in path */
                    return false;
                }

                x = x + colDir;
                y = y + rowDir;
            }
            return true;  /* if we get here, path was clear */
        }
        else {
            return false;  /* path is not a straight line */
        }
    }


    /* This routine executes a sequence of moves - mostly for Monkey right now.  Monkey can do a sequence of
    moves.  If there is another piece in the destination square of the move, then it is captured and removed from
    the board.
     */
    /**
        Determines if the sequence of moves is legal for a monkey piece. Verifies that the piece attempting to perform 
        more than one move is a monkey.
    */
    public void performMove(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GameBoard congoBoard) throws Exception {
        /* Method determines if the sequence of moves are legal for this piece */
        /* It also checks which GamePieces the owner of this piece has in the river at the beginning of the turn.
        If any of the player's river dwellers other than crocodile are still in the river upon completion of the turn,
        they will drown and be captured.
         */
        /* We are currently assuming that the first location in the arrays is the first square we are moving to.
        The current location of the piece is gotten from the GamePiece object
         */

        /* Get and store every piece that's in the river so we can determine who drowns at the end of this move */
        int activePlayer = getPlayer();
        ArrayList<GamePiece> riverDwellers = congoBoard.getRiverDwellers(activePlayer);

        if (ValidateMove(destRow, destCol, congoBoard.board)){
            int numMoves = destRow.size();

            if (jumpLinear(getRow(), getColumn(), destRow.get(0), destCol.get(0))){
                /* If first move is a jump then we know we have at least one jump or a sequence of jumps.  Otherwise it
                will be a single square move without capture and we can just move the monkey to his final square. */
                /* If it is a jump, we know that all the intermediary squares are empty.  We don't need
                to actually place the monkey in those.  Just place the monkey in his final location after jumping all the
                other squares.
                We do need to get and remove all the captured pieces caused by the monkey's jumps.
                We'll do that first.
                */
                int moveCounter = 0;
                int fromRow = getRow();
                int fromCol = getColumn();

                while (moveCounter < numMoves) {
                    /* get location piece is moving to */
                    int toRow = destRow.get(moveCounter);
                    int toCol = destCol.get(moveCounter);

                    /* check if we jumped a piece that needs to be captured */
                    GamePiece jumpedPiece = jumpCapturesPiece(fromRow, fromCol, toRow, toCol, congoBoard.board);
                    if (jumpedPiece != null) {
                        /* it should never be null for a monkey */
                        congoBoard.capturePiece(jumpedPiece);
                    }

                    /* save jumped to location as origin of next possible jump */
                    fromRow = toRow;
                    fromCol = toCol;
                    moveCounter++;
                }
            }

            /* move piece to its final location */
            int finalDestR = destRow.get(numMoves - 1);
            int finalDestC = destCol.get(numMoves - 1);
            congoBoard.movePiece(getRow(), getColumn(), finalDestR, finalDestC);

            /* now check if any river dwelling pieces are still in the river and need to drown and be captured */
            congoBoard.drownRiverDwellers(riverDwellers);
        }
        else throw new Exception("Invalid move!");
    }
}
