package Game;

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
        if (row == 4)
            return true;
        else
            return false;
    }

    public Boolean inRiver(int r){
        /* determines if specific location is in the river */
        if (r == 4)
            return true;
        else
            return false;
    }

}

