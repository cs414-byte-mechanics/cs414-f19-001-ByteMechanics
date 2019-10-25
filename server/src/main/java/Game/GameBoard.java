package Game;

import Game.GamePiece;
import Game.PawnPiece;
import Game.Player;

import java.util.ArrayList;
import java.util.ListIterator;

public class GameBoard{
    static int riverRow = 3;  /* river is on row 3 in the board */
    static int boardNumRows = 7;
    static int boardNumCols = 7;
    static int boardLowerIndex = 0;
    static int boardUpperIndex = 6;

    /* playing board with 49 squares */
    GamePiece board[][] = new GamePiece[boardNumRows][boardNumCols];

    public GameBoard(){
     /* initialize every board square to NULL */
        for (int i = 0; i < boardNumRows; i++){
            for (int j = 0; j < boardNumCols; j++) {
                board[i][j] = null;
            }
        }
        /* this assumes the players have already set up the pieces that we're placing on the board */
    }

    public GamePiece getGamePiece(int r, int c){
        if ((r >= 0) && (r < boardNumRows)
                && (c >= 0) && (c < boardNumCols)){
            return board[r][c];
        }
        else return null;
    }

    private void placeInitialPieces(int player){
        /* places all pieces on one side of the board for a specific player */
        int homeRow = (player == 1) ? 0 : 6;
        int pawnRow = (player == 1) ? 1 : 5;

        /* Create and setup animal game pieces for player */
        board[homeRow][0] = new GiraffePiece(homeRow, 0, player);
        board[homeRow][1] = new MonkeyPiece(homeRow,1,player);
        board[homeRow][2] = new ElephantPiece(homeRow,2,player);
        board[homeRow][3] = new LionPiece(homeRow,3,player);
        board[homeRow][4] = new ElephantPiece(homeRow,4,player);
        board[homeRow][5] = new CrocodilePiece(homeRow,5, player);
        board[homeRow][6] = new ZebraPiece(homeRow, 6, player);

        /* need to initialize all pawns */
        for (int i =0; i<=6; i++){
            board[pawnRow][i] = new PawnPiece(pawnRow, i, player);
        }
    }

    public void InitGameBoard(){
        /* Create and setup game pieces for player 1 */
        placeInitialPieces(1);

        /* Create and setup game pieces for player 2 */
        placeInitialPieces(2);

    }

    public String toString(){
        String row = "";
        String boardStr = "--------------\n";
        String playPiece;
        for (int i = boardNumRows - 1; i >= 0; i--){
            /* traverse each row one at a time */
            row = "|";
            for (int j = 0; j < boardNumCols; j++){
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

    public ArrayList<GamePiece> getRiverDwellers(int activePlayer){
        /* find all of the pieces this player has in the river - with the exception of the crocodile */
        ArrayList<GamePiece> riverDwellers = new ArrayList<GamePiece>();
        for (int i = 0; i < boardNumCols; i++){
            GamePiece piece = getGamePiece(riverRow,i);
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

    public void placePiece(GamePiece piece, int row, int col) {
        /* places a piece in a specific square on the board and update it's row, column fields */
        piece.row = row;
        piece.column = col;
        board[row][col] = piece;
    }

    public void movePiece(GamePiece piece, int row, int col) {
        /* places a piece in a specific square on the board and update it's row, column fields */
        /* sets original location of piece on the board to null */
        board[piece.getRow()][piece.getColumn()] = null;
        placePiece(piece, row, col);
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol){
        /* routine does NO error checking but assumes move is legal and updates the piece's info
           as well as set it's previous square location to NULL */

        GamePiece movingPiece = getGamePiece(fromRow,fromCol);
        GamePiece destPiece = getGamePiece(toRow,toCol);

        /* capture piece that is in the destination square */
        if (destPiece != null) {
            /* This piece will remain in the player's list (Player.playerPiece[]) of pieces so we
            need to mark it as captured */
            destPiece.setCaptured();
        }
        /* now move conquering piece onto the square */
        placePiece(movingPiece,toRow,toCol);
        this.board[fromRow][fromCol] = null;

        // when move a piece, if it is pawn and in land in row 6, we should turn on the super pawn flag
        if ( (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==1 && this.board[toRow][toCol].row == 6 ) ||
        (this.board[toRow][toCol] instanceof PawnPiece && this.board[toRow][toCol].player==2 && this.board[toRow][toCol].row == 0 ) ) {

            PawnPiece pawnPiece = (PawnPiece) getGamePiece(toRow,toCol);
            System.out.println(pawnPiece.row);
            pawnPiece.superPawn = true;

        }
    }

    public void capturePiece(GamePiece pieceToBeCaptured){
        pieceToBeCaptured.setCaptured();

        // remove the piece from the board
        this.board[pieceToBeCaptured.row][pieceToBeCaptured.column] = null;
    }

    /* Helper routine to check if move is not out of board */
    public static boolean validBoardLocation(int destRow, int destCol){
        if (destRow > boardUpperIndex || destRow < boardLowerIndex)
            return false;

        if (destCol > boardUpperIndex || destCol < boardLowerIndex)
            return  false;

        return true;
    }
}
