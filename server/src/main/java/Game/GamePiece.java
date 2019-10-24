package Game;

import Game.GameBoard;
import Game.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class GamePiece {
    public int row;
    public int column;
    public int player;  /* set to 1 or 2 to indicate which player owns the piece */
    public boolean captured;

    public GamePiece(){
    }

    public GamePiece(int r, int c, int p){
        row = r;
        column = c;
        player = p;
        captured = false;
    }

    @Override
    public String toString() {
        return ("pieceID: "+pieceIDString()+"\nrow: "+row+"\ncolumn: "+column+"\nplayer: "
                +player+"\ncaptured: "+captured+"\n");
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public int getPlayer(){
        return player;
    }

    public String pieceIDString(){
        return " ";
    }

    public void setCaptured(){
        /* indicates that piece is captured and no longer in play on the board */
        captured = true;
    }

    public boolean checkCaptured(){
        /* returns a value indicating if piece is captured */
        return captured;
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board){
        /* ValidateMove should always be called on a specific GamePiece - monkey, lion, etc
        Therefore, this routine is returning false so it never validates and incorrect move.
        This needs to be here so the use of ValidateMove() in performMove() will compile.
         */
        return false;
    }

    public boolean ValidateMove(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GamePiece[][] board){
        /* This method allows any piece other than monkey to handle recieving it's move specifications
        either using an array or individual integer values to indicate the square locations it is traversing.
         */
        /* Since all non-monkey pieces can only move one square at a time, the array can never contain more than
        one square location.
         */
        if (destRow.size()>1 || destCol.size()>1)
            return false;
        else
            return ValidateMove(destRow.get(0), destCol.get(0), board);
    }

    protected GamePiece jumpCapturesPiece(int fromRow, int fromCol, int destRow, int destCol, GameBoard board){
        /* Returns the object of a Gamepiece that was captured by jumping over it.
        Monkey is the only piece that captures by jumping.  All other pieces capture by landing on the square.
        So, this method returns NULL for any pieces that don't capture with a jump move.
         */
        return null;
    }

    public boolean orthogonalMove(int fromRow, int fromCol, int toRow, int toCol){
        /* Returns true if this is an orthogonal move, false if not */
        if ((fromRow == toRow) || (fromCol == toCol)){
            return true;
        }
        else return false;
    }

    public boolean diagonalMove(int fromRow, int fromCol, int toRow, int toCol){
        /* Returns true if this is a diagonal (45 degree) move, false if not */
        if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)){
            return true;
        }
        else return false;
    }

    public int manhattanDistance(int fromRow, int fromCol, int toRow, int toCol){
        /* Returns the manhattan distance associated with a moves coordinates */
        return (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol));
    }

    public boolean jumpLinear(int fromRow, int fromCol, int toRow, int toCol){
        if (orthogonalMove(fromRow, fromCol, toRow, toCol) &&
                manhattanDistance(fromRow, fromCol, toRow, toCol) == 2)
            return true;
        if (diagonalMove(fromRow, fromCol, toRow, toCol) &&
                manhattanDistance(fromRow, fromCol, toRow, toCol) == 4)
            return true;
        return false;
    }

    public Boolean inRiver(){
        /* determines if playing piece is currently in the river */
        return inRiver(getRow());
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
        if (destCol != getColumn()) return false;

        /* check for river crossing */
        if (((getRow() > destRow) && (destRow >= GameBoard.riverRow)) ||
                ((getRow() < destRow) && (destRow <= GameBoard.riverRow)))
            return true;
        else return false;
    }

    public Boolean pathClear(int destRow, int destCol, GamePiece[][] board){
        /* check to ensure no pieces along the path */
        /* WARNING - does not check that final destination is clear */
        int distCol = Math.abs(destCol - getColumn());
        int distRow = Math.abs(destRow - getRow());

        if ((destRow == this.row) && (destCol == this.column)) return true;  /* destination same as origin */

        if ((destRow == this.row) /* horizontal move */
            || (destCol == this.column) /* vertical move */
            || (distCol == distRow) /* diagonal move */
        ) {
            int rowDir = (distRow == 0) ? 0 : (destRow - this.row)/distRow;  /* -1 for left dir, +1 for right dir, 0 for horizontal path */
            int colDir = (distCol == 0) ? 0 : (destCol - this.column)/distCol; /* -1 for down dir, +1 for up dir, 0 for vertical path */

            int x = this.column + colDir;
            int y = this.row + rowDir;
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

    /* This routine executes one move for a specific piece other than Monkey.  Monkey can do a sequence of
    moves.  If there is another piece in the destination square of the move, then it is captured and removed from
    the board.
    NOTE - Monkey should use performMoveSeq()
     */
    public boolean performMove(int destRow, int destCol, GameBoard congoBoard) {
        /* Method determines if the move to (destRow, destCol) is a legal move for this piece */

        /* It also checks which GamePieces the owner of this piece has in the river at the beginning of the turn.
        If any of the player's river dwellers other than crocodile are still in the river upon completion of the turn,
        they will drown and be captured.
         */
        int activePlayer = this.player;
        ArrayList<GamePiece> riverDwellers = new ArrayList<GamePiece>();
        riverDwellers = congoBoard.getRiverDwellers(activePlayer);

        if (ValidateMove(destRow, destCol, congoBoard.board)){
            if (!(squareEmpty(destRow, destCol, congoBoard.board))){
                congoBoard.capturePiece(congoBoard.getGamePiece(destRow,destCol));
            }
            /* move is valid for this piece and opponent's piece, if any, has been captures
            - so update board by moving the piece
             */
            congoBoard.movePiece(this.row, this.column, destRow, destCol);
            /* check if we jumped a piece that needs to be captured */
            //GamePiece jumpedPiece = jumpCapturesPiece(this.row, this.column, destRow, destCol, congoBoard);

            /* now check if any river dwelling pieces are still in the river and need to drown and be captured */
            congoBoard.drownRiverDwellers(riverDwellers);
            return true;
        }
        else return false;
    }

    /* This routine executes a sequence of moves - mostly for Monkey right now.  Monkey can do a sequence of
    moves.  If there is another piece in the destination square of the move, then it is captured and removed from
    the board.
     */
    public boolean performMove(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GameBoard congoBoard) {
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
        ArrayList<GamePiece> riverDwellers = new ArrayList<GamePiece>();
        riverDwellers = congoBoard.getRiverDwellers(activePlayer);

        if (ValidateMove(destRow, destCol, congoBoard.board)){
            /* move was determined to be valid so let's sequence through the moves and perform them */
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
                    GamePiece jumpedPiece = jumpCapturesPiece(fromRow, fromCol, toRow, toCol, congoBoard);
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
            return true;
        }
        else return false;
    }

    public Boolean moveOneOrTwoStepStraightBackward (int destRow, int destCol, GamePiece[][] board){

        //check for one/two steps straight down
        int  distRow = Math.abs(destRow - this.row ) ;
        int distCol = Math.abs(destCol - this.column) ;

        if (( distRow == 2 && distCol == 0 ) || (distRow == 1 && distCol == 0 ))
            // if destination is empty not occupied by any pieces
            return (squareEmpty(destRow, destCol, board) && pathClear(destRow, destCol, board));
        else
            return false;
    }

    // move/capture side away
    public Boolean moveSideAwayForSuperPawn (int destRow , int destCol, GamePiece[][] board) {
        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        if (distRow == 0 && distCol == 1)
            return squareEmptyOrCapturable(destRow, destCol, board);

        else
            return false;
    }

    // move/capture one step forward
    public Boolean moveOneStepsStraightOrDiagonally(int destRow, int destCol, GamePiece[][] board){

        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        if ( (distRow == 1 && distCol == 0) || (distRow == 1 && distCol == 1))
            return squareEmptyOrCapturable(destRow, destCol, board);
        else
            return false;
    }

    public Boolean moveOneOrTwoStepsDiagonallyBackward (int destRow, int destCol, GamePiece[][] board) {

        int distRow = Math.abs(destRow - this.row);
        int distCol = Math.abs(destCol - this.column);

        //check for one/two steps diagonally down
        if ((distRow == 1 && distCol == 1 ) || (distRow == 2 && distCol == 2 ))
            // if destination is empty not occupied by any pieces
            return ( squareEmpty(destRow, destCol, board) && pathClear(destRow,destCol,board) );
        else
            return false;
    }

    // helper function for elephant move one step orthogonal
    public boolean MoveOneStepOrthogonal(int distRow, int distCol)
    {
        if ((distRow== 1 && distCol == 0 ) || (distRow== 0 && distCol == 1 ))
            return true;
        else
            return false;
    }
/*
    // helper function for elephant move two steps orthogonal
    public boolean MoveTwoStepOrthogonal(int distRow, int distCol)
    {
        if ((distRow == 2 && distCol == 0 ) || (distRow== 0 && distCol == 2))
            return true;
        else
            return false;
    }

    // helper routine for giraffe moves
    public boolean moveOneStepAnyDirection(int distRow, int distCol){
        if (distRow <=1 && distCol <=1 )
            return true;
        else return false;
    }

    // helper routine for giraffe to move two steps straight in any direction
    public boolean moveTwoStepsStraightAnyDirection(int distRow, int distCol) {
        if ((distRow == 0 && distCol ==2 ) || (distRow == 2 && distCol ==0))
            return true;
        else
            return false;
    }

    // helper routine to move two steps diagonally in any direction
    public boolean moveTwoStepsDiagonalAnyDirection(int distRow, int distCol){
        if(distRow == 2 && distCol == 2)
            return true;
        else
            return false;
    }
 */
}
