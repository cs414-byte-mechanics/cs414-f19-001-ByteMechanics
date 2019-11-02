package game ;

import game.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;

/*
  This class contains tests for the helper methods in the GamePiece class in the Congo game app.
 */

public class GamePiecesTest {
    GameBoard congoGame;

    // Setup to be done before every test in TestPlan
    @Before
    public void initialize() {
        congoGame = new GameBoard();
        congoGame.initialize();
    }

    @Test
    public void testCrocInRiver(){
        /* Start with initial board and test if crocodile is in river */
        GamePiece[][] congoBoard = congoGame.board;
        CrocodilePiece croc1 = (CrocodilePiece) congoBoard[0][5];
        CrocodilePiece croc2 = (CrocodilePiece) congoBoard[6][5];
        assertTrue(croc1.inRiver() == false);
        assertTrue(croc2.inRiver() == false);
        congoGame.movePiece(0,5,3,5);
        assertTrue(croc1.inRiver() == true);
        assertTrue(croc2.inRiver() == false);
    }

    @Test
    public void testMoveTowardRiver(){
        /* Start with initial */
        GamePiece[][] congoBoard = congoGame.board;
        CrocodilePiece croc1 = (CrocodilePiece) congoBoard[0][5];
        CrocodilePiece croc2 = (CrocodilePiece) congoBoard[6][5];
        assertTrue(croc1.moveTowardRiver(2,5) == true);  /* move to but not in river */
        assertTrue(croc1.moveTowardRiver(3,5) == true);  /* move into river */
        assertTrue(croc1.moveTowardRiver(4,5) == false);  /* move across river */
        assertTrue(croc1.moveTowardRiver(2,3) == false);  /* move diagonally towards river */

        assertTrue(croc2.moveTowardRiver(4,5) == true);  /* move to but not in river */
        assertTrue(croc2.moveTowardRiver(3,5) == true);  /* move into river */
        assertTrue(croc2.moveTowardRiver(2,5) == false);  /* move across river */
        assertTrue(croc2.moveTowardRiver(4,3) == false);  /* move diagonally towards river */

        /* move croc1 across the river */
        congoGame.movePiece(0,5,5,5);
        assertTrue(croc1.moveTowardRiver(3,5) == true);
        /* move croc back to south side of river */
        congoGame.movePiece(5,5,2,5);
        /* move him further south */
        assertTrue(croc1.moveTowardRiver(0,5) == false);
    }

    @Test
    public void testclearPath() {
        /* Start with initial board */
        CrocodilePiece croc1 = (CrocodilePiece) congoGame.board[0][5];

        assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);
        /*so let's just move the elephant forward to block */
        congoGame.movePiece(1,4,2,4);  /* move pawn forward to unblock crocodile */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);


        /* Once we have pawns let's turn on the following tests */
        congoGame.movePiece(2,4,3,4);  /* move pawn forward again to unblock crocodile */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);
        congoGame.movePiece(1,3,2,3);  /* move another pawn forward to block croc1 */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);
        congoGame.movePiece(2,3,3,3);  /* move same pawn forward and unblock croc1 */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);
        /*check zebra all the way to opposite corner 0,6 to 6,0 */
        ZebraPiece zebra1 = (ZebraPiece) congoGame.board[0][6];
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false);
        congoGame.movePiece(1,5,2,5);  /* move pawn in front of croc forward - doesn't unblock everything*/
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false); /* blocked at 3,3 and 5,1 */
        congoGame.movePiece(3,3,4,3);  /* move column 3 pawn forward but still blocked by pawn at 5,1 */
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false);
        congoGame.movePiece(5,1,4,1);  /* move opponent's pawn to unblock zebra */
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == true);
        /* and how about 6,0 to 0,6 */
        GiraffePiece giraffe2 = (GiraffePiece) congoGame.board[6][0];
        assertTrue(giraffe2.pathClear(0, 6, congoGame.board) == true);


        /* test diagonally across board from 0,0 to 6,6 */
        GiraffePiece giraffe = (GiraffePiece) congoGame.board[0][0];
        assertTrue(giraffe.pathClear(6, 6, congoGame.board) == false);
        congoGame.movePiece(1,1,2,1);  /* move pawn forward again to partially unblock giraffe */
        assertTrue(giraffe.pathClear(6, 6, congoGame.board) == false);
        congoGame.movePiece(5,5,5,4);  /* move opponent's pawn forward again to totally unblock giraffe */
        assertTrue(giraffe.pathClear(6, 6, congoGame.board) == true);

        /* now diagonally in opposite direction 6,6 to 0,0 */
        ZebraPiece zebra = (ZebraPiece) congoGame.board[6][6];
        assertTrue(zebra.pathClear(0, 0, congoGame.board) == true);


        /* test vertical move upward */
        congoGame.board[1][0] = null; /* remove pawn */
        assertTrue(giraffe.pathClear(5, 0, congoGame.board) == true); /* should be clear all the way to opponent's pawn */
        /* test vertical move downward */
        GiraffePiece giraffe1 = (GiraffePiece) congoGame.board[6][0];
        congoGame.board[5][0] = null; /* remove pawn */
        assertTrue(giraffe.pathClear(0, 0, congoGame.board) == true);

    }

    @Test
    public void testclearPathHorizontal() {
        /* Start with initial board */
        /* test horizontal */
        GiraffePiece giraffe = (GiraffePiece) congoGame.board[6][0];  /* player 2's giraffe */
        ZebraPiece zebra = (ZebraPiece) congoGame.board[0][6];  /* player 1's zebra */

        congoGame.movePiece(0,6,3,6);  /* move player 1's zebra to river */
        congoGame.movePiece(6,0,3,0);  /* move player 2's giraffe to river */
        assertTrue(giraffe.pathClear(3, 6, congoGame.board) == true);
        assertTrue(zebra.pathClear(3, 0, congoGame.board) == true);
    }

}
