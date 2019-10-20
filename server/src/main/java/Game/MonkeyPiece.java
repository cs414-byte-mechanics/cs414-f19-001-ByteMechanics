package Game;

import java.util.ArrayList;
import java.util.Arrays;

public class MonkeyPiece extends GamePiece{

    public MonkeyPiece(){
    }

    public MonkeyPiece(int row, int col, int player){
        super(row,col,player);
    }

    public String pieceIDString(){
        return (player == 1) ? "m" : "M";
    }

    private GamePiece moveCapturesPiece(int fromRow, int fromCol, int destRow, int destCol, GameBoard board){
        /* returns the object of a piece the monkey jumped over and captured */
        int manhattanDist = manhattanDistance(fromRow, fromCol, destRow, destCol);
        boolean orthoMove = orthogonalMove(fromRow, fromCol, destRow, destCol);
        boolean diagMove = diagonalMove(fromRow, fromCol, destRow, destCol);

        /* check if this move is jumping over another piece */
        if ((orthoMove && (manhattanDist == 2)) || (diagMove && (manhattanDist == 4))){
            /* determine coordinates of captured piece */
            int deltaX = Math.abs(fromCol - destCol)/2;
            int deltaY = Math.abs(fromRow - destRow)/2;
            int captureX = (fromCol < destCol) ? fromCol + deltaX : destCol + deltaX;
            int captureY = (fromRow < destRow) ? fromRow + deltaY : destRow + deltaY;
            /* get piece from game board and return */
            return board.getGamePiece(captureY, captureX);
        }
        else return null;
    }

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

        Boolean[][] captured = new Boolean[7][7];

        /* fill with false which indicates the piece hasn't been captured yet.  true indicates it's captured. */
        //Arrays.fill(captured, false);
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                captured[i][j] = false;
            }
        }
        int curR = this.row;
        int curC = this.column;

        int numMoves = destRow.size();
        int moveCounter = 0;

        while (moveCounter < numMoves) {
            /* get location piece is moving to */
            System.out.println("move counter "+moveCounter);
            int destR = destRow.get(moveCounter);
            int destC = destCol.get(moveCounter);
            System.out.println("moving to "+destR+","+destC);
            int distCol = Math.abs(destC - curC);
            int distRow = Math.abs(destR - curR);
            System.out.println("distance to move "+distRow+","+distCol);

            /* check for out of bounds moves */
            if (destR > 6 || destR < 0) {
                return false;
            }
            if (destC > 6 || destC < 0) {
                return false;
            }

            /* test if monkey's FIRST move is a single square in any direction */
            if (moveCounter == 0) {
                if ((distCol <= 1) && (distRow <= 1)) {
                /* Monkey is moving a single square in any direction.  Destination square must be empty.
                If it contains opponents piece or player's piece, move is invalid. */
                    System.out.println("Checking single square move to " + destR + "," + destC);
                    System.out.println("square empty " + squareEmpty(destR, destC, board));
                    return squareEmpty(destR, destC, board);
                }
            }

            /* otherwise first move (and all subsequent moves) is > a distance of 1 square */
            /* To be a valid move it must ve a vertical, horizontal or diagonal move of 2 squares and jump over
            an opponent's piece which will then be captured.
            */
                int manhattanDist = manhattanDistance(curR, curC, destR, destC);
                System.out.println("Manhattan distance "+distRow+","+distCol);
                /* A horizontal or vertical move of 2 squares will have manhattanDist = 2.
                A diagonal move will have manhattanDist = 4.
                */
                if (!((manhattanDist == 2) || (manhattanDist == 4))) {
                    return false;
                } else {
                    /* check that piece we are jumping is opponent's piece so that we can capture it */
                    /* get coordinates of square being jumped */
                    int jumpSquareRow = curR + (destR - curR) / 2;
                    int jumpSquareCol = curC + (destC - curC) / 2;
                    System.out.println("Jumped square = "+jumpSquareRow+","+jumpSquareCol);
                    System.out.println("player "+this.player);
                    System.out.println("piece owner "+board[jumpSquareRow][jumpSquareCol].player);
                    System.out.println("Empty jump sq? "+squareEmpty(jumpSquareRow, jumpSquareCol, board));
                    System.out.println("Empty dest sq? "+squareEmpty(destR, destC, board));
                    System.out.println("Captured? "+captured[jumpSquareRow][jumpSquareCol]);
                    if ((squareEmpty(jumpSquareRow, jumpSquareCol, board)) ||
                            !(squareEmpty(destR, destC, board)) ||
                            (board[jumpSquareRow][jumpSquareCol].player == this.player) ||
                            (captured[jumpSquareRow][jumpSquareCol])){
                        /* if square being jumped is empty OR contains a piece from the same player OR
                        piece has already been jumped then this is not a valid move sequence.
                        */
                        System.out.println("move failed");
                        return false;
                    } else {
                        /* move is valid.  Mark the jumped piece as captured and continue checking move sequence */
                        captured[jumpSquareRow][jumpSquareCol] = true;
                    }
                }

                moveCounter = moveCounter + 1;
                curR = destR;  /* move monkey forward to continue checking move sequence */
                curC = destC;


        }
        return true;
    }
}

