package Game ;

// import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.Assert.*;

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
        congoGame.initialize();
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

//    @Test // Added by Fari -- test to check capture function
//
//    public void testCaptureFunction(){
//
//        GamePiece[][] congoBoard = congoGame.board;
//
//        // identify some pieces and assign them to the pieces' array
//        GiraffePiece giraffe = (GiraffePiece) congoGame.board[0][0];
////        (GamePiece) playerPieces[0] = (GamePiece) giraffe;
//
//        MonkeyPiece monkey = (MonkeyPiece) congoGame.board[0][1] ;
////        playerPieces[1] = (GamePiece) monkey;
////
//        ElephantPiece elephant = (ElephantPiece) congoGame.board[0][2];
////        playerPieces[2] = (GamePiece) elephant;
////
//        congoGame.capturePiece( (GamePiece) giraffe, new GamePiece[] { (GiraffePiece) giraffe, (MonkeyPiece) monkey, (ElephantPiece) elephant} );
//    }
}
