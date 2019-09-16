package Game;

public class ZebraPiece extends GamePiece {
    public ZebraPiece(){
    }

    public ZebraPiece(int row, int col, int player){
        super(row,col,player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board) {
    /* A zebra moves and captures other pieces exactly the same as a knight piece in chess */

        /* check for out of bounds moves */
        if (destRow > 6 || destRow < 0) {
            return false;
        }
        if (destCol > 6 || destCol < 0) {
            return false;
        }

        /* zebra moves 2 squares laterally followed by 1 vertically OR
           1 square vertically followed by 2 laterally.  Total distance moved should be 3 squares.
         */
        int distance = Math.abs(destRow - this.row) + Math.abs(destCol - this.column);
        if (distance != 3){
            return false;
        }

        /* Destination square must be empty or contain opponents piece.  If it contains
           opponents piece then it will be captured.
         */
        int activePlayer = this.player;
        if (board[destRow][destCol] == null || board[destRow][destCol].player != activePlayer){
            return true;
        }
        else {
            return false;
        }
    }
}
