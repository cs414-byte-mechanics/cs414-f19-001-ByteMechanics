package Game ;

// import jdk.jfr.StackTrace;
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

    @Test/*Added by Fari -- test for lion valid move */
    public void testLionSimpleMove(){

        LionPiece lionP1 = (LionPiece) congoGame.board[0][3];
        LionPiece lionP2 = (LionPiece) congoGame.board[6][3];

        GamePiece[][] congoBoard = congoGame.board;

        congoGame.movePiece(0,3,2,4); //lion1 2,4
        congoGame.movePiece(6,3, 4,2); // lion2 in 4,2
        //congoGame.movePiece(3,3, 3,4);
        assertTrue(lionP1.ValidateMove(4,2, congoGame.board) == true); // lion could see each other and capture
        assertTrue(lionP1.ValidateMove(4,3, congoGame.board) == false); // could not see, no capture

        assertTrue(lionP1.ValidateMove(2,5, congoGame.board) == false); // out of castle
        assertTrue(lionP1.ValidateMove(1,5, congoGame.board) == false); // out of castle

        congoGame.movePiece(5,4,2,3);
        assertTrue(lionP1.ValidateMove(2,3, congoGame.board) == true); // capture opponents pawn
        assertTrue(lionP1.ValidateMove(1,3, congoGame.board) == false); // can not catch teammate

        congoGame.movePiece(2,4,1,2); // lion1 in 1,2
        assertTrue(lionP1.ValidateMove(2,2, congoGame.board) == true);
        assertTrue(lionP1.ValidateMove(1,1, congoGame.board) == false);

        congoGame.movePiece(1,2,2,2); // lion1 in 2,2
        assertTrue(lionP2.ValidateMove(2,2, congoGame.board) == true); // catch other line with no obstacle between
        assertTrue(lionP2.ValidateMove(4,1, congoGame.board) == false); // out of caste

        congoGame.movePiece(1,0,5,2);
        assertTrue(lionP2.ValidateMove(4,2, congoGame.board) == false);

        congoGame.movePiece(5,2,5,3);
        assertTrue(lionP2.ValidateMove(5,3, congoGame.board) == true);

        congoGame.movePiece(5,6,5,3);
        assertTrue(lionP2.ValidateMove(5,3, congoGame.board) == false);
    }

    @Test /* Added By Fari -- Test for elephant piece player1 moves  -- READY TO BE DELETED */
    public void testElephantP1SimpleMove() {
        /*Start with initial board and test is Player 1 elephant can move from (0,2) to (1,2), (2,2) */

        ElephantPiece elephant1P1 = (ElephantPiece) congoGame.board[0][2];
        ElephantPiece elephant2P1 = (ElephantPiece) congoGame.board[0][4];

        GamePiece[][] congoBoard = congoGame.board;
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == false); /*elephant1 can not move to square occupied by pawn*/
        assertTrue(elephant1P1.ValidateMove(2, 2, congoBoard) == true); /*but it can jump to 2nd square empty forward*/

        congoGame.movePiece(1,2, 2,2); // move pawn (1,2) to (2,2)
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == true); /*now elephant can move to empty square ahead */

//        congoGame.movePiece(1,4, 2,3); /*move pawn one step diagonally*/
        assertTrue(elephant2P1.ValidateMove(2, 4, congoBoard) == true); /* elephant can move 2 square up*/

        congoGame.movePiece(0,6,2,6);
        assertTrue(elephant2P1.ValidateMove(0, 6, congoBoard) == true); // move 2 steps right OK

        congoGame.movePiece(0,4,2,4);
        assertTrue(elephant2P1.ValidateMove(0, 4, congoBoard) == true); //  move 2 steps down OK

        congoGame.movePiece(2,2,3,2);
        assertTrue(elephant2P1.ValidateMove(2, 2, congoBoard) == true);  //move 2 steps left OK

        congoGame.movePiece(6,4,3,4);
        congoGame.movePiece(0,2,3,3);
        assertTrue(elephant1P1.ValidateMove(3, 4, congoBoard) == true);

        congoGame.movePiece(3,2,3,4);
        assertTrue(elephant1P1.ValidateMove(3, 2, congoBoard) == true);
    }

    @Test /* Added By Fari -- Test for elephant piece player2 moves */
    public void testElephantPlayer2SimpleMove() {

        /*Start with initial board for player2 */
//        System.out.println("run test");
        ElephantPiece elephant1P2 = (ElephantPiece) congoGame.board[6][2];
        ElephantPiece elephant2P2 = (ElephantPiece) congoGame.board[6][4];

        GamePiece[][] congoBoard = congoGame.board;

        assertTrue(elephant1P2.ValidateMove(4, 2, congoBoard) == true); // jump two step down from 6,2 to 4,2

        congoGame.movePiece(6,4,5,4);
        assertTrue(elephant1P2.ValidateMove(6, 4, congoBoard) == true); // jump two step right

        //pawn5p1 move to 6,4
        congoGame.movePiece(1,6,6,4);
        assertTrue(elephant1P2.ValidateMove(6, 4, congoBoard) == true); // jump right two steps capture opponents

        congoGame.movePiece(6,0,5,2);
        assertTrue(elephant1P2.ValidateMove(6, 0, congoBoard) == true); // jump left

        // opponents piece to 3,5
        congoGame.movePiece(0,0,3,5);
        // move elephant from 6,2 to 3,4
        congoGame.movePiece(6,2,3,4);
        assertTrue(elephant1P2.ValidateMove(3, 5, congoBoard) == true);

        congoGame.movePiece(3,4,3,6);
        assertTrue(elephant1P2.ValidateMove(3, 5, congoBoard) == true);
    }

    @Test /* Added by Fari- test for pawn pieces belong to player two*/
    public void testPawnPlayer2SimpleMove(){
        /*Start with initial board and test is Player2 */
        PawnPiece Pawn1P2 = (PawnPiece) congoGame.board[5][0];
        PawnPiece Pawn2P2 = (PawnPiece) congoGame.board[5][1];
        PawnPiece Pawn3P2 = (PawnPiece) congoGame.board[5][3];
        PawnPiece Pawn7P2 = (PawnPiece) congoGame.board[5][6];
        GamePiece[][] congoBoard = congoGame.board;

        // pawn2 can move from (5,0) to (4,0) ? yes
        assertTrue(Pawn1P2.ValidateMove(4, 0, congoBoard) == true);
        assertTrue(Pawn1P2.ValidateMove(6, 0, congoBoard) == false); // not backward

        //pawn2p2 move from (5,1) to 4,1 when pawn3p1 is in 4,1
        congoGame.movePiece(1,2, 4,1);
        assertTrue(Pawn2P2.ValidateMove(4, 1, congoBoard) == true);

        //Pawn2P2 move from 5,1 to 1,1
        congoGame.movePiece(5,1, 1,1);
        assertTrue(Pawn2P2.ValidateMove(1, 2, congoBoard) == false); // can move side? no it's not a super pawn
        assertTrue(Pawn2P2.ValidateMove(3, 1, congoBoard) == true); // other side of river can move backward two step straight
        assertTrue(Pawn2P2.ValidateMove(0, 2, congoBoard) == true); // it moves forward one step diagonally
        assertTrue(Pawn2P2.ValidateMove(2, 0, congoBoard) == false); // how about diagonally backward? no cause it is not super pawn yet

        //Pawn2P2 move from 1,1 to 0,4 . Now it is a super pawn
        congoGame.movePiece(1,1,0,4);
        assertTrue(Pawn2P2.ValidateMove(0, 5, congoBoard) == true); // can move side by one square
        assertTrue(Pawn2P2.ValidateMove(0, 3, congoBoard) == true);
//        assertTrue(Pawn2P2.ValidateMove(1, 5, congoBoard) == true); // failed. maybe occupied

        System.out.println(congoGame.toString());
        System.out.println("Pawn2P2 "+Pawn2P2.row+","+Pawn2P2.column+","+Pawn2P2.player+","+Pawn2P2.superPawn);
        assertTrue(Pawn2P2.ValidateMove(2, 6, congoBoard) == false); // Diagonally 2 step backward works!!
        /* changed above test since it appears to be blocked by a pawn - MLN */
        assertTrue(Pawn2P2.ValidateMove(2, 2, congoBoard) == false); // Diagonally 2 step backward works!!
        /* I believe this is also blocked by a pawn.  Should be false?  - MLN */

        // move super pawn player2 to 2,6
        congoGame.movePiece(0,4,2,6);
        assertTrue(Pawn2P2.ValidateMove(3, 5, congoBoard) == true); // can move backward one square diagonally ? yes
//        assertTrue(Pawn2P2.ValidateMove(4, 4, congoBoard) == true);

        congoGame.movePiece(2,6, 2,5);
        assertTrue(Pawn2P2.ValidateMove(3, 4, congoBoard) == true); // Diagonally one step backward Works!!!?yes
        assertTrue(Pawn2P2.ValidateMove(1, 4, congoBoard) == true); // Diagonally one step forward? yes
        assertTrue(Pawn2P2.ValidateMove(3, 6, congoBoard) == true); // Diagonally one step backward Works!!!?yes

        ////Pawn2P2 move from 2,5 to 3,3
        congoGame.movePiece(2,5,3,3);
        assertTrue(Pawn2P2.ValidateMove(2 , 4 , congoBoard) == true); // one step diagonally forward ? yes
        assertTrue(Pawn2P2.ValidateMove(4 , 2 , congoBoard) == true); // one step diagonally ?
        assertTrue(Pawn2P2.ValidateMove(4 , 4 , congoBoard) == true); // 2 steps backward diagonally works ?

        congoGame.movePiece(3,3,1,4);
        assertTrue(Pawn2P2.ValidateMove(3 , 2 , congoBoard) == true);
//        assertTrue(Pawn2P2.ValidateMove(3 , 6 , congoBoard) == true);

        congoGame.movePiece(1,4,2,4);
        assertTrue(Pawn2P2.ValidateMove(0, 6 , congoBoard) == false); //2 steps diagonally forward ? no
        assertTrue(Pawn2P2.ValidateMove(0, 2 , congoBoard) == false); // 2 steps diagonally forward ? no
        assertTrue(Pawn2P2.ValidateMove(1, 3 , congoBoard) == true); // one step diagonally forward? yes
        assertTrue(Pawn2P2.ValidateMove(1, 4 , congoBoard) == true); // one step straight forward
        assertTrue(Pawn2P2.ValidateMove(3, 4 , congoBoard) == true);

        assertTrue(Pawn2P2.ValidateMove(4, 2 , congoBoard) == true); // move backward diagonally 2 steps? yes
//        assertTrue(Pawn2P2.ValidateMove(4, 6 , congoBoard) == true); // failed maybe something between as obstacle

        congoGame.movePiece(2,4, 2,3);
        assertTrue(Pawn2P2.ValidateMove(4, 5 , congoBoard) == true); // but change position and try again? 2 step back diagonally works

        congoGame.movePiece(5,3, 0,3);
        assertTrue(Pawn3P2.ValidateMove(0, 2 , congoBoard) == true); // super pawn

        assertTrue(Pawn3P2.ValidateMove(1, 2, congoBoard) == true);
        assertTrue(Pawn3P2.ValidateMove(1, 4, congoBoard) == true);
        assertTrue(Pawn3P2.ValidateMove(2, 1, congoBoard) == true);

    }


    @Test /* Added By Fari -- Test for Giraffe piece valid moves */
    public void testGiraffeP1SimpleMove(){
        /*Start with initial board and test is Player 1 giraffe can move from (0,0) to (1,0) */

        GiraffePiece giraffe = (GiraffePiece) congoGame.board[0][0];
        GamePiece[][] congoBoard = congoGame.board;

        /*move giraffe to wrong place*/

        assertTrue(giraffe.ValidateMove(2, 0, congoBoard) == true); // 2 steps straight forward
        assertTrue(giraffe.ValidateMove(2, 2, congoBoard) == true); //2 steps straight diagonally
        assertTrue(giraffe.ValidateMove(2, 1, congoBoard) == false);
        assertTrue(giraffe.ValidateMove(1, 1, congoBoard) == false); // can't move one step diagonally cause pawn p1 is there

        congoGame.movePiece(1,1,2,1);
        assertTrue(giraffe.ValidateMove(1, 1, congoBoard) == true); //move pawn one step forward so it can move to empty square


        congoGame.movePiece(1,0,2,0); /*move pawn first from (1,0) to (2,0)*/
        assertTrue(giraffe.ValidateMove(1, 0, congoBoard) == true); // if giraffe can move to empty square

        congoGame.movePiece(2,0,1,0);
        assertTrue(giraffe.ValidateMove(1, 0, congoBoard) == false); // can capture ? no, square is occupied

        congoGame.movePiece(6,1, 2,2); /*move opponent monkey from (6,1) to (2,2)*/
        assertTrue(giraffe.ValidateMove(2, 2, congoBoard) == true); // can giraffe move and capture opponent's ?

    }


    @Test /* Added By Fari -- Test for Giraffe piece player2 */
    public void testGiraffeP2SimpleMove(){
        GiraffePiece giraffeP2 = (GiraffePiece) congoGame.board[6][0];
        GamePiece[][] congoBoard = congoGame.board;

        congoGame.movePiece(5,0,4,0);
        congoGame.movePiece(5,1,4,0);
        assertTrue(giraffeP2.ValidateMove(5, 0, congoBoard) == true);
        assertTrue(giraffeP2.ValidateMove(5, 1, congoBoard) == true);

        // move giraffe p2 to 4,3
        congoGame.movePiece(6,0,4,3);
        assertTrue(giraffeP2.ValidateMove(3, 2, congoBoard) == true); // one step forward diagonal
        assertTrue(giraffeP2.ValidateMove(3, 4, congoBoard) == true); // one step forward diagonal
        assertTrue(giraffeP2.ValidateMove(2, 1, congoBoard) == true); // two steps diagonally forward
        congoGame.movePiece(5,4,5,5);
        assertTrue(giraffeP2.ValidateMove(5, 4, congoBoard) == true); // one step diagonally back

        congoGame.movePiece(6,1,6,2);
        assertTrue(giraffeP2.ValidateMove(6, 1, congoBoard) == true); // from 4,3 to 6,1?yes
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
