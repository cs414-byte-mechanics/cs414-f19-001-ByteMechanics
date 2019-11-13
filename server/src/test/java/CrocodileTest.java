package Game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
  This class contains tests for the Crocodile piece in Congo.
 */

public class CrocodileTest {
    GameBoard congoGame;

    // Setup to be done before every test in TestPlan
    @Before
    public void initialize() {
        congoGame = new GameBoard();
        congoGame.initialize();
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

    @Test(expected = Exception.class)
    public void testCrocPerformBlockedMove() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to (4,5) */
        /* Crocodile is blocked by pawn piece. */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        movesRow.add(4);
        movesCol.add(5);
        croc.performMove(movesRow, movesCol, congoGame);
    }

    @Test
    public void testCrocPerformMoveWithNoCapture() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 2 crocodile can move from (6,6) to (5,5) */
        /* after pawn has been moved out of the way */

        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        /* capture player 1's pawn at (1,4) forward to (2,4) so crocodile can make a 1 square diagonal move.
         */
        congoGame.movePiece(1,4,2,4);
        movesRow.add(1);
        movesCol.add(4);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame) == true);
        /* check that source location is empty */
        assertTrue(congoGame.getGamePiece(0,5) == null);
        /* check that GamePiece got updated correctly */
        assertTrue(croc.row == 1);
        assertTrue(croc.column == 4);
    }

    @Test
    public void testCrocPerformMoveWithCapture() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to (3,5) */
        /* and Captures pawn at (3,5) */

        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        /* remove player 2's pawn at (5,5) and then move player 1's pawn from (1,5) to (3,5) to
        setup this test
         */
        congoGame.capturePiece(congoGame.getGamePiece(5,5)); /* remove player 2's pawn */
        congoGame.movePiece(1,5,3,5);  /* move player 1's pawn to the river */

        movesRow.add(3);
        movesCol.add(5);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame) == true);
        /* check that source location of crocodile is now empty */
        assertTrue(congoGame.getGamePiece(6,5) == null);
        /* check that GamePiece got updated correctly */
        assertTrue(croc.row == 3);
        assertTrue(croc.column == 5);
    }

    @Test(expected = Exception.class)
    public void testCrocMovesTowardRiverFailure() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to other squares along that file */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(5,5);  /* pawn in front of crocodile */
        /* move pawn out of the way across the river */
        movesRow.add(4);
        movesCol.add(4);
        pawn.performMove(movesRow, movesCol, congoGame);
        movesRow.set(0,3);
        pawn.performMove(movesRow, movesCol, congoGame);
        movesRow.set(0,2);
        pawn.performMove(movesRow, movesCol, congoGame);
        /* move crocodile diagonally out of its home row */
        movesRow.set(0,5);
        movesCol.set(0,5);
        croc.performMove(movesRow, movesCol, congoGame);
        /* make sure that crocodile can't move multiple squares beyond the river */
        movesRow.set(0,2);
        movesCol.set(0,5);
        croc.performMove(movesRow, movesCol, congoGame);
    }
    @Test
    public void testCrocMovesTowardRiver() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 2 crocodile can move from (6,5) to other squares along that file */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(6,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(5,5);  /* pawn in front of crocodile */
        /* move pawn out of the way across the river */
        movesRow.add(4);
        movesCol.add(4);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,3);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,2);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        /* move crocodile diagonally out of its home row */
        movesRow.set(0,5);
        movesCol.set(0,5);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        assertTrue(congoGame.getGamePiece(6,5) == null); /* check that original board square of croc is empty now */
        /* make sure that crocodile can't move multiple squares beyond the river */
        movesRow.set(0,2);
        movesCol.set(0,5);
        /* but it can move into the river if path is clear */
        movesRow.set(0,3);
        movesCol.set(0,5);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        assertTrue(croc.inRiver());
    }

    @Test(expected = Exception.class)
    public void testCrocDrowningFailure() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can move from (0,5) to (3,5) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0, 5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1, 5);  /* pawn in front of crocodile */
        /* attempt to move croc to river but it will fail since pawn is blocking the move */
        movesRow.add(3);
        movesCol.add(5);
        croc.performMove(movesRow, movesCol, congoGame);
    }

    @Test
    public void testCrocDrowning() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can move from (0,5) to (3,5) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1,5);  /* pawn in front of crocodile */
        /* attempt to move croc to river but it will fail since pawn is blocking the move */
        movesRow.add(3);
        movesCol.add(5);
        /* now move pawn out of the way across the river */
        movesRow.set(0,2);
        movesCol.set(0,5);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,3);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,4);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,5);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));  /* this will actually be a capture of opponent's pawn */
        /* then move croc to river */
        movesRow.set(0,3);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        assertTrue(croc.inRiver());

        /* now move another pawn piece and make sure crocodile doesn't drown */
        PawnPiece pawn2 = (PawnPiece) congoGame.getGamePiece(1,0);
        movesRow.set(0,2);
        movesCol.set(0,0);
        assertTrue(pawn2.performMove(movesRow, movesCol, congoGame));
        assertTrue(croc.inRiver());
    }

    @Test(expected = Exception.class)
    public void testCrocMoveInRiverFailure() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can move from (0,5) to (3,5) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0, 5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1, 4);
        /* first try to capture player's own pawn on diagonal path */
        movesRow.add(1);
        movesCol.add(4);
        croc.performMove(movesRow, movesCol, congoGame);
    }

    @Test
    public void testCrocMoveInRiver() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can move from (0,5) to (3,5) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1,4);
        /* first try to capture player's own pawn on diagonal path */
        movesRow.add(1);
        movesCol.add(4);
        /* move pawn out of the way (across the river) so it is not blocking */
        movesRow.set(0,2);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,3);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,4);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        /* now attempt to move crocodile diagonally again */
        movesRow.set(0,1);
        movesCol.set(0,4);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        /* now move to river */
        movesRow.set(0,3);
        movesCol.set(0,4);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        /* then move along river */
        movesRow.set(0,3);
        movesCol.set(0,0);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        assertTrue(croc.inRiver());
        /* now move to other end of river */
        movesRow.set(0,3);
        movesCol.set(0,6);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
        assertTrue(croc.inRiver());
        /* check to make sure we have a crocodile piece on the board in (3,6) at end of river */
        assertTrue(congoGame.getGamePiece(3,6) instanceof CrocodilePiece);
    }

    @Test(expected = Exception.class)
    public void testCrocMoveDiagToRiverFailure() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can't move from (0,5) to (3,2) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1,4);
        /* first move pawn out of the way - across the river*/
        movesRow.add(2);
        movesCol.add(4);
        movesRow.set(0,3);
        movesRow.set(0,4);
        /* now crocodile out of home row with a diagonal move */
        movesRow.set(0,1);
        movesCol.set(0,4);
        /* now try moving to river along a diagonal path */
        movesRow.set(0,3);
        movesCol.set(0,2);
        croc.performMove(movesRow, movesCol, congoGame);
    }

    @Test
    public void testCrocMoveDiagToRiver() throws Exception {
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();

        /* Start with initial board and test if Player 1 crocodile can't move from (0,5) to (3,2) in the river */
        CrocodilePiece croc = (CrocodilePiece) congoGame.getGamePiece(0,5);
        PawnPiece pawn = (PawnPiece) congoGame.getGamePiece(1,4);
        /* first move pawn out of the way - across the river*/
        movesRow.add(2);
        movesCol.add(4);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,3);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        movesRow.set(0,4);
        assertTrue(pawn.performMove(movesRow, movesCol, congoGame));
        /* now crocodile out of home row with a diagonal move */
        movesRow.set(0,1);
        movesCol.set(0,4);
        assertTrue(croc.performMove(movesRow, movesCol, congoGame));
    }

    //@Test
    public void testCrocArray2StepCapture() throws Exception {
        /* Start with initialized board */
        /* tests to make sure crocodile captures 1 piece like a rook towards the river */
        GamePiece myCroc = congoGame.getGamePiece(0,5);
        GamePiece opponentPawn = congoGame.getGamePiece(5,5);
        GamePiece myPawn = congoGame.getGamePiece(1,5);

        congoGame.movePiece(opponentPawn, 2, 5);  /* move opponent's pawn to 2,5 on my side of the river */

        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();
        /* try to perform move to 2,5 to capture opponent's pawn, but move is blocked by myPawn */
        movesRow.add(2);
        movesCol.add(5);

        assertTrue(myCroc.performMove(movesRow, movesCol, congoGame) == false);  /* fails since it's blocked by myPawn */

 //       assertTrue(congoGame.getGamePiece(2,5) != null);  /* opponentPawn is on board */
        /* move myPawn out of the way to enable capture move */
        congoGame.movePiece(myPawn, 2, 4);
        assertTrue(myCroc.performMove(movesRow, movesCol, congoGame));  /* cleared path to opponentPawn so move succeeds */
        assertTrue(congoGame.getGamePiece(0,5) == null);  /* crocodile has moved and left square empty */
        assertTrue(congoGame.getGamePiece(2,5) instanceof CrocodilePiece);  /* opponentPawn has been captured */

    }

}
