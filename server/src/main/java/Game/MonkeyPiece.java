package Game;

public class MonkeyPiece extends GamePiece{

    public MonkeyPiece(){
    }

    public MonkeyPiece(int row, int col, int player){
        super(row,col,player);
    }

    public boolean ValidateMove(int NextRow, int NextCol) {


        return true;
    }
}
