package Game;

public class GiraffePiece extends GamePiece  {
    /* initial constructor*/
    public GiraffePiece(){
    }

    public GiraffePiece(int row, int col, int player){
        super(row, col, player);
    }

    public boolean ValidateMove(int destRow, int destCol, GamePiece[][] board)
    {
        /* The Giraffe moves and captures by jumping to the second square in a straight or diagonal direction.
        A jump cannot be blocked by interposing pieces of either color.
        In addition it has the king's move, without the right to capture with it.
        */
        /* check for out of bound moves */
        if (destRow > 6 || destRow < 0){
            return false;
        }
        if (destCol > 6 || destCol < 0){
            return  false;
        }
        int activePlayer = this.player ;

        /* check if Giraffe moved/jumped by two steps straight OR diagonal forward and it CAN capture*/
        if ((destRow - this.row == 2 && destCol - this.column == 0) || (destRow - this.row ==2 && destCol - this.column == 2)){
            if (board[destRow][destCol] == null || board[destRow][destCol].player != activePlayer){
                return true;
            }
            else{
                return false;
            }
        }

        /* check if Giraffe moved by one step straight OR diagonal in any direction and it CANNOT capture */
        else if ((destRow - this.row == 0 && destCol - this.column == 1) || (destRow - this.row == 1 && destCol - this.column == 1) || (destRow - this.row == 1 && destCol - this.column == 0)
                || (destRow - this.row == 1 && destCol - this.column == -1) || (destRow - this.row == 0 && destCol - this.column == -1) || (destRow - this.row == -1 && destCol - this.column == -1)
                || (destRow - this.row == -1 && destCol - this.column == 0) || (destRow - this.row == -1 && destCol - this.column == 1) ) {

            if (board[destRow][destCol] == null){
                return true;
            }
            else{
                return false; }
        }
        return true; // Not sure if we need it ????
    }



}
