package Game;

public class ZebraPiece extends GamePiece {
    public ZebraPiece(){
    }

    public ZebraPiece(int row, int col, int player){
        super(row,col,player);
    }

    public String pieceIDString(){
        return (player == 1) ? "z" : "Z";
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
           1 square vertically followed by 2 laterally.  Total distance moved should be 3 squares in an 'L' shape.
         */
        int distCol = Math.abs(destCol - this.column);
        int distRow = Math.abs(destRow - this.row);
        if (!((distCol == 2 && distRow == 1) || (distCol == 1 && distRow == 2))){
            return false;
        }

        /* Destination square must be empty or contain opponents piece.  If it contains
           opponents piece then it will be captured.
         */
        return squareEmptyOrCapturable(destRow, destCol, board);
    }

//    public boolean performMove(int destRow, int destCol, GameBoard congoBoard, Player player) {
//        if (ValidateMove(destRow, destCol, congoBoard.board)){
//            if (!(squareEmpty(destRow, destCol, congoBoard.board))){
//                congoBoard.capturePiece(congoBoard.board[destRow][destCol], player.playerPieces);
//            }
//
//            congoBoard.movePiece(this.row, this.column, destRow, destCol);
//            return true;
//        }
//        else return false;
//    }
}
