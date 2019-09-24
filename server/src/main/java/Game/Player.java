package Game;

public class Player {

    // every team has 14 single pieces, including 7 animals and 7 pawns
    static int numberOfPlayer = 14;

    // to identify which player it is? one or two?
    int playerID;

    // An array to store every player's pieces (totally 14) - every single piece is a game piece class like Pawn piece
    GamePiece playerPiece[] = new GamePiece[numberOfPlayer];

    public void initPlayerPieces(){

        // Initialize every single piece of each player
        // if player one, all pieces row is 0 and all playerID are 1
        // if player two, all pieces are on rows = 6 and playerID is 2
        playerPiece[0] = new GiraffePiece( playerID == 1 ? 0: 6, 0, playerID == 1 ? 1:2);
        playerPiece[1] = new MonkeyPiece( playerID == 1 ? 0: 6, 1, playerID == 1 ? 1:2);
        playerPiece[2] = new ElephantPiece( playerID == 1 ? 0: 6, 2, playerID == 1 ? 1:2);
        playerPiece[3] = new LionPiece( playerID ==1 ? 0: 6, 3, playerID == 1 ? 1:2);
        playerPiece[4] = new ElephantPiece( playerID == 1 ? 0: 6, 4, playerID == 1 ? 1:2);
        playerPiece[5] = new CrocodilePiece( playerID == 1 ? 0: 6, 5, playerID == 1 ? 1:2);
        playerPiece[6] = new ZebraPiece( playerID ==1 ? 0: 6, 6, playerID == 1 ? 1:2);

        playerPiece[7] = new PawnPiece( playerID ==1 ? 1: 5, 0, playerID == 1 ? 1:2);
        playerPiece[8] = new PawnPiece( playerID ==1 ? 1: 5, 1, playerID == 1 ? 1:2);
        playerPiece[9] = new PawnPiece( playerID ==1 ? 1: 5, 2, playerID == 1 ? 1:2);
        playerPiece[10] = new PawnPiece( playerID ==1 ? 1: 5, 3, playerID == 1 ? 1:2);
        playerPiece[11] = new PawnPiece( playerID ==1 ? 1: 5, 4, playerID == 1 ? 1:2);
        playerPiece[12] = new PawnPiece( playerID ==1 ? 1: 5, 5, playerID == 1 ? 1:2);
        playerPiece[13] = new PawnPiece( playerID ==1 ? 1: 5, 6, playerID == 1 ? 1:2);

    }
}
