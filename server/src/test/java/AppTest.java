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

        /* ADDED BY Fari: check row, column and player number for each pawn*/
        for (int r = 1; r <= 5; r = r + 4) {
            //check both rows - 1, 5 including pawns
            int playr = r / 5 + 1;
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

        /* ADDED BY Fari: Check if all Pawns initialized! */
        for (int r = 1 ; r <= 5; r = r + 4){
//             check both rows of pawns
            for (int c = 0; c <= 6; c= c + 1 ){
                assertTrue((congoGame.board[r][c] instanceof PawnPiece ) == true);
            }
        }
    }

    @Test
    public void testZebraSimpleMove() {
        /*Start with initial board and test if Player 1 zebra can move from (0,6) to (2,5) */
        ZebraPiece zebra = congoGame.zebraP1;
        assertTrue(zebra.ValidateMove(2,5,congoGame.board) == true);
    }

    @Test/*Added by Fari -- test for lion valid move */
    public void testLionSimpleMove(){
        LionPiece lion = congoGame.lionP1;
//        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(lion.ValidateMove(2,5,congoGame.board) == false); // ***Failed

    }


    @Test /* Added By Fari -- Test for elephant piece valid moves */
    public void testElephant1SimpleMove() {
        /*Start with initial board and test is Player 1 elephant can move from (0,2) to (1,2), (2,2) */
        System.out.println("run test");
        ElephantPiece elephant1P1 = (ElephantPiece) congoGame.board[0][2];
        ElephantPiece elephant2P1 = (ElephantPiece) congoGame.board[0][4];

        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == false); /*elephant1 can not move to square occupied by pawn*/
        assertTrue(elephant1P1.ValidateMove(2, 2, congoBoard) == true); /*but it can jump to 2nd square empty forward*/

        congoGame.movePiece(1,2, 2,2); // move pawn (1,2) to (2,2)
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == true); /*now elephant can move to empty square ahead */

        congoGame.movePiece(1,4, 2,3); /*move pawn one step diagonally*/
        assertTrue(elephant2P1.ValidateMove(2, 4, congoBoard) == true); /* elephant can move 2 square up*/

    }

    /* Added By Fari -- Test for elephant piece valid moves */
//    @Test
//    public void testElephant2SimpleMove() {
//        /*Start with initial board and test is Player 1 elephant can move from (0,4) to (1,4), (2,4) */
//        System.out.println("run test");
//        ElephantPiece elephant2 = congoGame.elephant2P1;
//        GamePiece[][] congoBoard = congoGame.board;
//        assertTrue(elephant2.ValidateMove(1, 4, congoBoard) == true);
//        assertTrue(elephant2.ValidateMove(2, 5, congoBoard) == true);
//    }

    /* Added By Fari -- Test for Giraffe piece valid moves */
    @Test
    public void testGiraffeSimpleMove(){
        /*Start with initial board and test is Player 1 giraffe can move from (0,0) to (1,0) */
        System.out.println("run test");
        GiraffePiece giraffe = (GiraffePiece) congoGame.board[0][0];
        GamePiece[][] congoBoard = congoGame.board;

        /*move giraffe to wrong place*/
        assertTrue(giraffe.ValidateMove(2, 0, congoBoard) == false);
        assertTrue(giraffe.ValidateMove(2, 2, congoBoard) == false);
        assertTrue(giraffe.ValidateMove(2, 1, congoBoard) == false);

        congoGame.movePiece(1,0,2,0); /*move pawn first from (1,0) to (2,0)*/
        assertTrue(giraffe.ValidateMove(1, 0, congoBoard) == true); // if giraffe can move to empty square

        congoGame.movePiece(6,1, 2,2); /*move opponent monkey from (6,1) to (2,2)*/
//        assertTrue(giraffe.ValidateMove(2, 2, congoBoard) == true); // can giraffe capture it ? ***Failed
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

    /* Added By Fari -- Test for elephant piece wrong moves */
//    @Test
//    public void testElephantBlockedMove() {
//        /*Start with initial board and test is Player 1 elephant can move from (0,2) to (1,2) and (2,2) but with same player */
//        ElephantPiece elephant = congoGame.elephantP1;
//        GamePiece[][] congoBoard = congoGame.board;
//        congoBoard[1][2] = congoGame.pawnP1;
//        assertTrue(elephant.ValidateMove(1,2, congoBoard) == false);
//    }

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
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == false);
        /*so let's just move the elephant forward to block */
        congoGame.movePiece(1,4,2,4);  /* move elephant forward to unblock zebra */
        assertTrue(croc1.pathClear(5, 0, congoGame.board) == true);

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
        assertTrue(zebra1.pathClear(6, 0, congoGame.board) == false);
    }

    @Test
    public void testCrocSimpleMove() {
        /*Start with initial board and test if Player 1 crocodile can move from (0,5) to (1,4) */
        CrocodilePiece croc1 = (CrocodilePiece) congoGame.board[0][5];
        assertTrue(croc1.ValidateMove(1,4,congoGame.board) == false);
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
