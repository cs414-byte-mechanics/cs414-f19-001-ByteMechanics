package Game;

import Game.GameBoard;

public class LionPiece extends GamePiece {
    /* initial constructor*/
    public LionPiece() {
    }

    public LionPiece(int row, int col, int player) {
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GameBoard[][] board ) {
        /*The Lion moves like a chess king, but may not leave his castle at his side of the river.
        In addition, lions can capture other lions if they `see' it, i.e.,
        if there is a vertical or diagonal line with no pieces between the two lions, the lion may jump to the other lion and capture it.*/

        // check for out of castle for lion is down side
        if ((destRow >= 0 && destRow <= 2 && destCol >= 2 && destCol <= 4)){

            /*Check for chess king move, to any arbitrary direction just by one step */
            /* calculate distance to destination */
            int distrow = Math.abs(destRow - this.row);
            int distcol = Math.abs(destCol - this.column);

            /*move one step to an empty square or capture if there is any opponent's piece  */
            if ( distrow <= 1 || distcol <=1  ){
                return squareEmptyOrCapturable(destRow, destCol, board);
            }
            else if (board[destRow][destCol] != null){
                System.print.out(board[destRow][destCol]);
            }

        }
        return true;

//         //check for out of castle moves
//        if ((destRow >= 0 && destRow <= 2 && destCol >= 2 && destCol <= 4)) {
//
//            //lion is in one side of river in his castle, now check for chess king moves
//
//            if ((destRow - this.row == 0 && destCol - this.column == 1) || (destRow - this.row == 1 && destCol - this.column == 1)
//                    || (destRow - this.row == 1 && destCol - this.column == 0) || (destRow - this.row == 1 && destCol - this.column == -1)
//                    || (destRow - this.row == 0 && destCol - this.column == -1) || (destRow - this.row == -1 && destCol - this.column == -1)
//                    || (destRow - this.row == -1 && destCol - this.column == 0) || (destRow - this.row == -1 && destCol - this.column == 1) ){
//
//                int activePlayer = this.player;
//                if (board[destRow][destCol] == null || (board[destRow][destCol].player != activePlayer)) {
//                    return true;
//                }
//                else {
//                    return false;}
//            }
//           // lions can capture other lions if they `see' it, with no pieces in middle
//            else{
//                System.out.print("Need to implement capturing the other lion with no obstacle in middle");
//                }
//
//        }
//        //check for lion is in the other side of river in his castle
//        else if ((destRow >= 4 && destRow <= 6 && destCol >= 2 && destCol <= 4)) {
//             //lion is in the other side of castle, now check for chess king moves
//            if ((destRow - this.row == 0 && destCol - this.column == 1) || (destRow - this.row == 1 && destCol - this.column == 1)
//                    || (destRow - this.row == 1 && destCol - this.column == 0) || (destRow - this.row == 1 && destCol - this.column == -1)
//                    || (destRow - this.row == 0 && destCol - this.column == -1) || (destRow - this.row == -1 && destCol - this.column == -1)
//                    || (destRow - this.row == -1 && destCol - this.column == 0) || (destRow - this.row == -1 && destCol - this.column == 1)) {
//
//                int activePlayer = this.player;
//
//                if (board[destRow][destCol] == null || (board[destRow][destCol].player != activePlayer)) {
//                    return true;
//                }
//                else {
//                    return false;
//                }
//            }
//            else {
//                System.out.print("Need to implement capturing the other lion with no obstacle in middle");
//            }
//        }
//
//        return true; // Not sure
    }
    }