package Game ;

import jdk.jfr.StackTrace;
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

//        /* ADDED BY Fari: check row, column and player number for each pawn*/
//        for (int r = 1; r <= 5; r = r + 4) {
//            //check both rows - 1, 5 including pawns
//            int playr = r / 6 + 1;
//            for (int c = 0; c <= 6; c = c + 1) {
//                assertTrue(congoGame.board[r][c].player == playr);
//                assertTrue(congoGame.board[r][c].row == r);
//                assertTrue(congoGame.board[r][c].column == c);
//            }
//        }

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

//        /* ADDED BY Fari: Check if all Pawns initialized! */
//        for (int r = 1 ; r <= 5; r = r + 4){
//            /* check both rows of pawns*/
//            for (int c = 0; c <= 6; c= c + 1 ){
//                assertTrue((congoGame.board[r][c] instanceof PawnPiece ) == true);
//            }
//        }
    }

    @Test
    public void testZebraSimpleMove() {
        /*Start with initial board and test is Player 1 zebra can move from (0,6) to (2,5) */
        System.out.println("run test");
        ZebraPiece zebra = congoGame.zebraP1;
        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(zebra.ValidateMove(2,5,congoBoard) == true);
    }


    @Test /* Added By Fari -- Test for elephant piece valid moves */
    public void testElephant1SimpleMove() {
        /*Start with initial board and test is Player 1 elephant can move from (0,2) to (1,2), (2,2) */
        System.out.println("run test");
        ElephantPiece elephant1 = congoGame.elephant1P1;
        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(elephant1.ValidateMove(1, 2, congoBoard) == true);
        assertTrue(elephant1.ValidateMove(2, 2, congoBoard) == true);
    }

    /* Added By Fari -- Test for elephant piece valid moves */
    @Test
    public void testElephant2SimpleMove() {
        /*Start with initial board and test is Player 1 elephant can move from (0,4) to (1,4), (2,4) */
        System.out.println("run test");
        ElephantPiece elephant2 = congoGame.elephant2P1;
        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(elephant2.ValidateMove(1, 4, congoBoard) == true);
        assertTrue(elephant2.ValidateMove(2, 5, congoBoard) == true);
    }

    /* Added By Fari -- Test for Giraffe piece valid moves */
    @Test
    public void testGiraffeSimpleMove(){
        /*Start with initial board and test is Player 1 giraffe can move from (0,0) to (2,0), (2,2) */
        System.out.println("run test");
        Giraffe giraffe = congoGame.giraffeP1;
        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(giraffe.ValidateMove(2, 0, congoBoard) == true);
        assertTrue(giraffe.ValidateMove(2, 2, congoBoard) == true);
    }

    @Test
    public void testZebraBlockedMove() {
        /*Start with initial board and test is Player 1 zebra can move from (0,6) to (2,5) */
        ZebraPiece zebra = congoGame.zebraP1;
        GamePiece[][] congoBoard = congoGame.board;
        congoBoard[2][5] = congoGame.crocodileP1;
        assertTrue(zebra.ValidateMove(2,5,congoBoard) == false);
    }

//    /* Added By Fari -- Test for elephant piece wrong moves */
//    @Test
//    public void testElephantBlockedMove() {
//        /*Start with initial board and test is Player 1 elephant can move from (0,2) to (1,2) and (2,2) but with same player */
//        ElephantPiece elephant = congoGame.elephantP1;
//        GamePiece[][] congoBoard = congoGame.board;
//        congoBoard[1][2] = congoGame.pawnP1;
//        assertTrue(elephant.ValidateMove(1,2, congoBoard) == false);
//    }
}
