package Game;

import java.util.ArrayList;

public class MonkeyPiece extends GamePiece{

    public MonkeyPiece(){
    }

    public MonkeyPiece(int row, int col, int player){
        super(row,col,player);
    }

    public String pieceIDString(){
        return (player == 1) ? "m" : "M";
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board){
        return false;
    }

    protected GamePiece jumpCapturesPiece(int fromRow, int fromCol, int destRow, int destCol, GamePiece[][] board) {
        /* Returns the object of a Gamepiece that was captured by jumping over it.
        Monkey is the only piece that captures by jumping.  All other pieces capture by landing on the square.
         */
        /* returns the object of a piece jumped over and captured if this is a legal jump */
        /* assumes destination is a legal board square */
        int manhattanDist = manhattanDistance(fromRow, fromCol, destRow, destCol);
        boolean orthoMove = orthogonalMove(fromRow, fromCol, destRow, destCol);
        boolean diagMove = diagonalMove(fromRow, fromCol, destRow, destCol);

        /* check that destination square is empty to land in */
        if (squareEmpty(destRow, destCol, board)){
        /* check if this move is performing a straight line jump of 2 squares */
            if ((orthoMove && (manhattanDist == 2)) || (diagMove && (manhattanDist == 4))) {
            /* get coordinates of captured piece */
                int deltaX = Math.abs(fromCol - destCol) / 2;
                int deltaY = Math.abs(fromRow - destRow) / 2;
                int captureX = (fromCol < destCol) ? fromCol + deltaX : destCol + deltaX;
                int captureY = (fromRow < destRow) ? fromRow + deltaY : destRow + deltaY;
                /* get piece from game board and return */
                //GamePiece capturedPiece = board.getGamePiece(captureY, captureX);
                GamePiece capturedPiece = board[captureY][captureX];
                if (capturedPiece.getPlayer() == getPlayer()) {
                    /* capturedPiece must be an opponent's piece.  We can't jump our own piece. */
                    capturedPiece = null;
                }
                return capturedPiece;
            }
        }

        return null;
    }

    public Boolean[][] initializeCaptureArray(){
        Boolean[][] captured = new Boolean[7][7];  /* array tracks which pieces have been jumped and can't be jumped twice */

        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                captured[i][j] = false;
            }
        }
        return captured;
    }

    public boolean validateJumpSequence(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GamePiece[][] board) {
        Boolean[][] captured = initializeCaptureArray();  /* tracks which pieces have been jumped and can't be jumped twice */
        int numMoves = destRow.size();

        int fromRow = getRow();
        int fromCol = getColumn();
        int toRow, toCol;
        for (int i = 0; i < numMoves; i++){
            toRow = destRow.get(i);
            toCol = destCol.get(i);
            if (GameBoard.inBounds(toRow, toCol)){
                GamePiece piece = jumpCapturesPiece(fromRow, fromCol, toRow, toCol, board);
                if ((piece != null) && (captured[piece.getRow()][piece.getColumn()] == false)){
                    /* move is a legal jump capture over an opponent's piece which hasn't been jumped yet
                    in this move sequence */
                    captured[piece.getRow()][piece.getColumn()] = true;
                }
                else return false;
            }
            else return false;
            fromRow = toRow;
            fromCol = toCol;

        }
        return true;
    }

    public boolean validate1StepMove(int destRow, int destCol, GamePiece[][] board) {
        if (GameBoard.inBounds(destRow, destCol)) {
            if (validMove1SquareAnyDirection(getRow(), getColumn(), destRow, destCol)) {
                return squareEmpty(destRow, destCol, board);
            }
        }
        return false;
    }

        @Override
    public boolean ValidateMove(ArrayList<Integer> destRow, ArrayList<Integer> destCol, GamePiece[][] board) {
    /* A monkey has two basic move types - the second of which can be chained together to form a sequence of captures.
            1) It can move a single step in any direction similar to a King piece in chess
                but without the capability to capture the opponent's piece.
            2) It can also jump over an adjacent piece in an orthogonal or diagonal direction.
                - This results in the capture of the opponent's piece.
                - The square landed on after jumping & capturing must be empty.
                - These jump/capture moves can be chained subsequent to the following rules -
                    * A given piece can only be jumped once.
                    * Successive jumps can be in different directions.
                    * A given square can be visited more than once.
                    * Captured pieces are removed after the entire sequence of jumps is completed.
            */

        int destR = destRow.get(0);  /* first location to move to */
        int destC = destCol.get(0);  /* first location to move to */
        if (!(jumpLinear(getRow(), getColumn(), destR, destC))){
            /* Monkey is making a single square move since the first move isn't a jump */
            return validate1StepMove(destR, destC, board);
        }
        else{
            /* Monkey is making a sequence of moves */
            return validateJumpSequence(destRow, destCol, board);
        }

    }

}

