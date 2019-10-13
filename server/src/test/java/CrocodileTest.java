package Game ;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.Assert.*;
import Game.GamePiece;
import Game.GameBoard;
import Game.Player;

import java.util.ArrayList;
import java.util.Arrays;

/*
  This class contains tests for the Crocodile piece in Congo.
 */

public class CrocodileTest {
    GameBoard congoGame;
    Player congoPlayer1;
    Player congoPlayer2;

    // Setup to be done before every test in TestPlan
    @Before
    public void initialize() {
        congoGame = new GameBoard();
        congoGame.InitGameBoard();
        congoPlayer1 = new Player(1);
        congoPlayer1.initPlayerPieces(congoGame);
        congoPlayer2 = new Player(2);
        congoPlayer2.initPlayerPieces(congoGame);
    }

    @Test
    public void testCrocSimpleMoveValidate() {
        /*Start with initial board and test if Player 1 crocodile can move from (0,5) to (1,4) */
        CrocodilePiece croc1 = (CrocodilePiece) congoGame.getGamePiece(0,5);

        /* test a blocked move - pawn is blocking croc */
        assertTrue(croc1.ValidateMove(1,4,congoGame.board) == false);
        /* test that croc can't move more than 1 square */
        assertTrue(croc1.ValidateMove(2,3,congoGame.board) == false);

        /* move pawn out of the way and then move croc */
        congoGame.movePiece(1,4,2,5);  /* move pawn to (2,5)) */
        /* now croc should be able to move 1 square */
        assertTrue(croc1.ValidateMove(1,4,congoGame.board) == true);
        congoGame.movePiece(0,5,1,4);  /* move crocodile to (1,4)) */
        assertTrue(croc1.ValidateMove(3,4,congoGame.board) == true);  /* move to river? */
        congoGame.movePiece(1,4,3,4);  /* move crocodile to (3,4)) */
        assertTrue(croc1.ValidateMove(4,5,congoGame.board) == true);  /* move to other bank of river? */

        congoGame.movePiece(3,4,4,5);  /* move crocodile diagonally to (4,5)) */
        assertTrue(croc1.ValidateMove(5,5,congoGame.board) == true);  /* move to other bank of river? */
        congoGame.movePiece(4,5,5,5);  /* move crocodile vertical to (5,5)) */
        assertTrue(croc1.ValidateMove(3,5,congoGame.board) == true);  /* move back to river? */
        assertTrue(croc1.ValidateMove(3,3,congoGame.board) == false);  /* move diagonally back to river? */
    }

    @Test
    public void testCrocPerformBlockedMove() {
        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to (4,5) */
        /* Crocodile is blocked by pawn piece. */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        assertTrue(croc.performMove(4, 5, congoGame) == false);
    }

    @Test
    public void testCrocPerformMoveWithNoCapture() {
        /* Start with initial board and test if Player 2 crocodile can move from (6,6) to (5,5) */
        /* after pawn has been moved out of the way */

        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        /* capture player 1's pawn at (1,4) forward to (2,4) so crocodile can make a 1 square diagonal move.
         */
        congoGame.movePiece(1,4,2,4);
        assertTrue(croc.performMove(1, 4, congoGame) == true);
        /* check that source location is empty */
        assertTrue(congoGame.getGamePiece(0,5) == null);
        /* check that GamePiece got updated correctly */
        assertTrue(croc.row == 1);
        assertTrue(croc.column == 4);
    }

    @Test
    public void testCrocPerformMoveWithCapture() {
        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to (3,5) */
        /* and Captures pawn at (3,5) */

        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        /* remove player 2's pawn at (5,5) and then move player 1's pawn from (1,5) to (3,5) to
        setup this test
         */
        congoGame.capturePiece(congoGame.getGamePiece(5,5)); /* remove player 2's pawn */
        congoGame.movePiece(1,5,3,5);  /* move player 1's pawn to the river */

        assertTrue(croc.performMove(3, 5, congoGame) == true);
        /* check that source location of crocodile is now empty */
        assertTrue(congoGame.getGamePiece(6,5) == null);
        /* check that player array of pieces has crocodile */
        assertTrue(congoPlayer2.playerPieces[5] != null);
        /* check that GamePiece got updated correctly */
        assertTrue(croc.row == 3);
        assertTrue(croc.column == 5);
        assertTrue(croc.checkCaptured() == false);
        assertTrue(congoPlayer1.playerPieces[12].checkCaptured());
    }

}
