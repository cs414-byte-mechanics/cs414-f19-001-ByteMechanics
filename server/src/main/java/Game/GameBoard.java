package Game;

import java.util.ArrayList;
import java.util.ListIterator;

public class GameBoard{
    public static final int NUM_ROWS = 7;
    public static final int NUM_COLUMNS = 7;
    public static final int RIVER_ROW = 3;

    public GamePiece[][] board;

    public GameBoard(){
        board = new GamePiece[NUM_ROWS][NUM_COLUMNS];
    }
    
    public void initialize(){
        placePiecesForPlayer(1);
        placePiecesForPlayer(2);
    }
    
    public String toString(){
        String row = "";
        String boardStr = "--------------\n";
        String playPiece;
        for (int i = NUM_ROWS - 1; i >= 0; i--){
            /* traverse each row one at a time */
            row = "|";
            for (int j = 0; j < NUM_COLUMNS; j++){
                /* traverse all columns in this row to see what pieces are on the board */
                GamePiece piece = getGamePiece(i,j);
                playPiece = (piece == null) ? " " : piece.pieceIDString();

                row = row + playPiece + "|";
            }
            row = row + "\n";
            row = row + "--------------\n";
            boardStr = boardStr + row;
        }

        return boardStr;
    }
    
    public void loadGame(String[][] board){
    }

    public GamePiece getGamePiece(int row, int col){
        if(inBounds(row, col)){
            return board[row][col];
        }
        else return null;
    }

    private void placePiecesForPlayer(int player){
        /* places all pieces on one side of the board for a specific player */
        int animalRow = (player == 1) ? 0 : 6;
        int pawnRow = (player == 1) ? 1 : 5;

        /* Create and setup animal game pieces for player */
        board[animalRow][0] = new GiraffePiece(animalRow, 0, player);
        board[animalRow][1] = new MonkeyPiece(animalRow,1,player);
        board[animalRow][2] = new ElephantPiece(animalRow,2,player);
        board[animalRow][3] = new LionPiece(animalRow,3,player);
        board[animalRow][4] = new ElephantPiece(animalRow,4,player);
        board[animalRow][5] = new CrocodilePiece(animalRow,5, player);
        board[animalRow][6] = new ZebraPiece(animalRow, 6, player);

        /* need to initialize all pawns */
        for (int i =0; i<=6; i++){
            board[pawnRow][i] = new PawnPiece(pawnRow, i, player);
        }
    }


    public ArrayList<GamePiece> getRiverDwellers(int activePlayer){
        /* find all of the pieces this player has in the river - with the exception of the crocodile */
        ArrayList<GamePiece> riverDwellers = new ArrayList<GamePiece>();
        for (int i = 0; i < NUM_COLUMNS; i++){
            GamePiece piece = getGamePiece(RIVER_ROW,i);
            if ((piece != null ) && (piece.player == activePlayer) && !(piece instanceof CrocodilePiece)){
                /* add to list if it's active player's piece and not a crocodile */
                riverDwellers.add(piece);
            }
        }
        return riverDwellers;
    }

    public void drownRiverDwellers(ArrayList<GamePiece> listRiverDwellers){
        // Create the ListIterator
        ListIterator listIter = listRiverDwellers.listIterator();

        // Iterating through the list of river dwellers to see if they are still in the river
        while(listIter.hasNext()){
            GamePiece dweller = (GamePiece) listIter.next();
            if (dweller.inRiver()){
                /* if piece is still in the river, it will be drowned */
                capturePiece(dweller);
            }
        }
    }
    
    public boolean inBounds(int row, int col){
        return row < NUM_ROWS && row >= 0 && col < NUM_COLUMNS && col >= 0;
    }

    public void movePiece(GamePiece piece, int row, int col) {
        if(inBounds(row, col)){
            int startingRow = piece.getRow();
            int startingCol = piece.getColumn();
            piece.row = row;
            piece.column = col;
            board[row][col] = piece;
            board[startingRow][startingCol] = null;
        }

    }

    /**
        routine does NO error checking but assumes move is legal and updates the piece's info as well as set it's previous square location to NULL
    */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){
        GamePiece movingPiece = getGamePiece(fromRow,fromCol);

        movePiece(movingPiece, toRow, toCol);
        
        if(movingPiece instanceof PawnPiece){
            checkForSuperPawn(movingPiece);
        }
    }
    
    public void checkForSuperPawn(GamePiece piece){
        //If the pawn reaches the other side of the board, it's a super pawn
        if ( (piece.getPlayer() == 1 && piece.getRow() == 6 ) || (piece.getPlayer() == 2 && piece.getRow() == 0 )) {
            PawnPiece pawn = (PawnPiece) piece;
            pawn.superPawn = true;
        }
    }

    public void capturePiece(GamePiece pieceToBeCaptured){
        // remove the piece from the board
        board[pieceToBeCaptured.row][pieceToBeCaptured.column] = null;
    }
}
