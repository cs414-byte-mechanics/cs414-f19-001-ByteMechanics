package Game;

import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import Game.GamePiece;
import Game.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;

/*
  This class contains tests for the Congo game app.
 */
//@RunWith(JUnit4.class)
public class AppTest {
    GameBoard congoGame;

    // Setup to be done before every test in TestPlan
    @Before
    public void initialize() {
        congoGame = new GameBoard();
        congoGame.InitGameBoard();
        System.out.println("initialize board");
    }

    @Test
    public void checkInit() {

        /* check row, column & player number for each piece */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows - 0,6 */
            int playr = r / 6 + 1;
            for (int c = 0; c <= 6; c = c + 1) {
                assertTrue(congoGame.board[r][c].player == playr);
                assertTrue(congoGame.board[r][c].row == r);
                assertTrue(congoGame.board[r][c].column == c);
            }
        }

        /*check if Giraffe pieces initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of giraffes - 0,6 */
            assertTrue((congoGame.board[r][0] instanceof GiraffePiece) == true);
        }

        /* check if Monkeys initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of monkeys - 0,6 */
            assertTrue((congoGame.board[r][1] instanceof MonkeyPiece) == true);
        }

        /* check if Elephants initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of elephants - 0,6 */
            for (int c = 2; c <= 4; c = c + 2) {
                /* check both columns of elephants - 2,4 */
                assertTrue((congoGame.board[r][c] instanceof ElephantPiece) == true);
            }
        }

        /* check if Lions are initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of lions - 0,6 */
            assertTrue((congoGame.board[r][3] instanceof LionPiece) == true);
        }

        /* check if Crocodiles initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of crocodiles - 0,6 */
            assertTrue((congoGame.board[r][5] instanceof CrocodilePiece) == true);
        }

        /* check if Zebras initialized */
        for (int r = 0; r <= 6; r = r + 6) {
            /* check both rows of crocodiles - 0,6 */
            assertTrue((congoGame.board[r][6] instanceof ZebraPiece) == true);
        }



    }

    @Test
    public void testMonkeyMove() {
        /* Start with initial board */
        /* this test assumes there are no pawns on the board yet */
        MonkeyPiece monkey1 = (MonkeyPiece) congoGame.board[0][1];
        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();
        movesRow.add(1);
        movesCol.add(0);

        /* jump to 1,0 */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true); /* change to false after pawns added */
        movesCol.set(0,1);  /* try jumping to 1,1 */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true); /* change to false after pawns added */
        movesCol.set(0,2);  /* try jumping to 1,2 */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true); /* change to false after pawns added */
        movesRow.set(0,2); /* try jumping to 2,2 which is not a straight line move */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == false);

        /* move the opponent's zebra to 1,2 */
        congoGame.movePiece(6,6,1,2);  /* move zebra from 6,6 to 1,2 so it is capturable with a jump */
        /* make first move in sequence -> 2,3 */
        movesRow.set(0,2);
        movesCol.set(0,3);
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true);
        congoGame.movePiece(6,0,1,4);  /* move opponent's giraffe to 1,4 */
        /* will not be capturable because crocodile is blocking */
        movesRow.add(0);  /* add jump to 0,5, but this should be blocked */
        movesCol.add(5);  /* Now have 2 moves in our sequence */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == false);
        /* now move the crocodile and allow the jump */
        congoGame.movePiece(0,5,1,6);  /* move crocodile from 0,5 to 1,6 so monkey can capture opponent's giraffe */
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true);

    }

    @Test
    public void testMonkeyCantJumpTwice() {
        /* Start with initial board */
        /* this test assumes there are no pawns on the board yet */
        /* tests to make sure we block move if piece is jumped twice */
        congoGame.movePiece(0,1,2,1);  /* initialize player's monkey to 2,1 */
        congoGame.movePiece(6,2,3,2);  /* initialize opponent's elephant to 3,2 */
        congoGame.movePiece(6,4,2,4);  /* initialize opponent's elephant to 2,4 */
        congoGame.movePiece(6,1,4,2);  /* initialize opponent's monkey to 4,2 */
        System.out.println("board \n"+ congoGame.toString());

        ArrayList<Integer> movesRow = new ArrayList<Integer>();
        ArrayList<Integer> movesCol = new ArrayList<Integer>();
        /* perform valid double jump (2,1) to (4,3) to (4,1) */
        movesRow.add(4);
        movesCol.add(3);
        movesRow.add(4);
        movesCol.add(1);
        MonkeyPiece monkey1 = (MonkeyPiece) congoGame.board[2][1];
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == true);
        /* now try and do an illegal move by jumping the first elephant again to get to the second elephant */
        /* add jumps to (2,3) to (2,5) */
        movesRow.add(2);
        movesCol.add(3);
        movesRow.add(2);
        movesCol.add(5);
        assertTrue(monkey1.ValidateMove(movesRow,movesCol,congoGame.board) == false);
    }

    @Test
    public void testZebraSimpleMove() {
        /*Start with initial board and test if Player 1 zebra can move from (0,6) to (2,5) */
        ZebraPiece zebra = congoGame.zebraP1;
        assertTrue(zebra.ValidateMove(2,5,congoGame.board) == true);
    }

    @Test
    public void testZebraBlockedMove() {
        /* Start with initial board and put Crocodile at row 2, col 3 to block move */
        /* Test if Player 1 zebra move from (0,6) to (2,5) is blocked */
        ZebraPiece zebra = congoGame.zebraP1;
        GamePiece[][] congoBoard = congoGame.board;
        congoBoard[2][5] = congoGame.crocodileP1;
        assertTrue(zebra.ValidateMove(2,5,congoBoard) == false);
    }

    @Test
    public void testZebraMove3Fail() {
        /* Start with initial board and test is Player 1 zebra can move from (0,6) to (3,6) */
        /* This move is illegal for Zebra */
        ZebraPiece zebra = congoGame.zebraP1;
        assertTrue(zebra.ValidateMove(3,6,congoGame.board) == false);
    }

    @Test
    public void testCrocInRiver(){
        /* Start with initial board and test if crocodile is in river */
        GamePiece[][] congoBoard = congoGame.board;
        CrocodilePiece croc1 = congoGame.crocodileP1;
        CrocodilePiece croc2 = congoGame.crocodileP2;
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
        CrocodilePiece croc1 = (CrocodilePiece) congoGame.board[0][5];
        CrocodilePiece croc2 = (CrocodilePiece) congoGame.board[6][5];
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
        /* no pawns yet on the board */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);
        /*so let's just move the elephant forward to block */
        congoGame.movePiece(0,4,1,4);  /* move elephant forward to unblock zebra */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);

        /* Once we have pawns let's turn on the following tests */
        //assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);
        //congoGame.movePiece(1,4,2,4);  /* move pawn forward to unblock croc1 */
        //assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);
        //congoGame.movePiece(1,3,2,3);  /* move another pawn forward to block croc1 */
        //assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);
        //congoGame.movePiece(2,3,3,3);  /* move same pawn forward and unblock croc1 */
        //assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);
        /*check zebra all the way to opposite corner */
        ZebraPiece zebra1 = (ZebraPiece) congoGame.board[0][6];
        //assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false);
        //congoGame.movePiece(1,5,2,5);  /* move pawn in front of croc forward - doesn't unblock everything*/
        //assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false);
        //congoGame.movePiece(5,1,4,1);  /* move opponent's pawn to unblock zebra */
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == true);
    }

    @Test
    public void testCrocSimpleMove() {
        /*Start with initial board and test if Player 1 crocodile can move from (0,5) to (1,4) */
        CrocodilePiece croc1 = (CrocodilePiece) congoGame.board[0][5];
        assertTrue(croc1.ValidateMove(1,4,congoGame.board) == true);
        assertTrue(croc1.ValidateMove(2,3,congoGame.board) == false);
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


}
