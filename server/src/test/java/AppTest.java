package Game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

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
    public void checkInit(){

        /*check if Giraffe pieces initialized */
        //GiraffePiece giraffe00 = congoBoard[0][0];
        //assertTrue((congoGame.board[0][0] instanceof GiraffePiece) == true);
        //assertTrue(congoGame.board[0][0].player == 1);
        //assertTrue(congoGame.board[0][0].row == 0);
        //assertTrue(congoGame.board[0][0].column == 0);
        //assertTrue((congoBoard[6][0] instanceof GiraffePiece) == true);
        //assertTrue(congoBoard[6][0].player == 2);
        //assertTrue(congoBoard[6][0].row == 6);
        //assertTrue(congoBoard[6][0].column == 0);

        /* check if Monkeys initialized */
        assertTrue((congoGame.board[0][1] instanceof MonkeyPiece) == true);
        assertTrue(congoGame.board[0][1].player == 1);
        assertTrue(congoGame.board[0][1].row == 0);
        assertTrue(congoGame.board[0][1].column == 1);
        assertTrue((congoGame.board[6][1] instanceof MonkeyPiece) == true);
        assertTrue(congoGame.board[6][1].player == 2);
        assertTrue(congoGame.board[6][1].row == 6);
        assertTrue(congoGame.board[6][1].column == 1);

        /* check if Crocodiles initialized */
        assertTrue((congoGame.board[0][5] instanceof CrocodilePiece) == true);
        assertTrue(congoGame.board[0][5].player == 1);
        assertTrue(congoGame.board[0][5].row == 0);
        assertTrue(congoGame.board[0][5].column == 5);
        assertTrue((congoGame.board[6][5] instanceof CrocodilePiece) == true);
        assertTrue(congoGame.board[6][5].player == 2);
        assertTrue(congoGame.board[6][5].row == 6);
        assertTrue(congoGame.board[6][5].column == 5);

        /* check if Zebras initialized */
        assertTrue((congoGame.board[0][6] instanceof ZebraPiece) == true);
        assertTrue(congoGame.board[0][6].player == 1);
        assertTrue(congoGame.board[0][6].row == 0);
        assertTrue(congoGame.board[0][6].column == 6);
        assertTrue((congoGame.board[6][6] instanceof ZebraPiece) == true);
        assertTrue(congoGame.board[6][6].player == 2);
        assertTrue(congoGame.board[6][6].row == 6);
        assertTrue(congoGame.board[6][6].column == 6);



    }

    @Test
    public void testZebraSimpleMove() {
        /*Start with initial board and test is Player 1 zebra can move from (0,6) to (2,5) */
        System.out.println("run test");
        ZebraPiece zebra = congoGame.zebraP1;
        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(zebra.ValidateMove(2,5,congoBoard) == true);
    }

    @Test
    public void testZebraBlockedMove() {
        /*Start with initial board and test is Player 1 zebra can move from (0,6) to (2,5) */
        ZebraPiece zebra = congoGame.zebraP1;
        GamePiece[][] congoBoard = congoGame.board;
        congoBoard[2][5] = congoGame.crocodileP1;
        assertTrue(zebra.ValidateMove(2,5,congoBoard) == false);
    }

}
