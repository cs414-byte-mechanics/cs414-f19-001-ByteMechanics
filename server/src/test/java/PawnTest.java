package game ;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

/*This class contains several tests for the Pawn piece */
public class PawnTest {
    GameBoard congoGame;

    // before any test, we need to initiate players and GameBoard
    @Before
    public void initialize(){
        // initialize board
        congoGame = new GameBoard();
        congoGame.initialize();
    }

    @Test
    public void testPawnP1Move(){
        /*Start with initial board and test is Player 1 */
        PawnPiece Pawn1P1 = (PawnPiece) congoGame.board[1][0];

        GamePiece[][] congoBoard = congoGame.board;

         //pawn1 can move from (1,0) to (2,0) ? yes
        assertTrue(Pawn1P1.ValidateMove(2, 0, congoBoard) == true);
        // pawn1 can move from (1,0) to (2,1) ? yes
        assertTrue(Pawn1P1.ValidateMove(2, 1, congoBoard) == true);
        // how about (1,0 ) to (1,1)?? no side away
        assertTrue(Pawn1P1.ValidateMove(1, 1, congoBoard) == false);
        // let see it can move backward to 00?? no
        assertTrue(Pawn1P1.ValidateMove(0, 0, congoBoard) == false);

        // let's move pawn piece to across the river and see he can move backward
        congoGame.movePiece(1,0,4,2); // move first to (4,2)
        assertTrue(Pawn1P1.ValidateMove(3, 2, congoBoard) == true);
        // move pawn p1 from (4,2) to (5,3)
        congoGame.movePiece(4,2,5,3);
        assertTrue(Pawn1P1.ValidateMove(6, 3, congoBoard) == true);
        assertTrue(Pawn1P1.ValidateMove(5, 4, congoBoard) == false); // he is not still super pawn
    }

    @Test
    public void pawn1MoveTest(){

        PawnPiece Pawn1P1 = (PawnPiece) congoGame.board[1][5];
        GamePiece[][] congoBoard = congoGame.board;

        // pawn 1 is on (6,3)
        congoGame.movePiece(1,5,6,3); // now he is super pawn
        // can go from 6,3 to 6,4 ??? yes
        assertTrue(Pawn1P1.ValidateMove(6, 4, congoBoard) == true); // side walk ? yes
        // how bout 6,3 t0 2,3??no
        assertTrue(Pawn1P1.ValidateMove(2, 3, congoBoard) == false);
        // 6,3 to 5,4 ? no
        assertTrue(Pawn1P1.ValidateMove(5, 4, congoBoard) == false); // in backward move can not capture
        // 6,3 to 4,3
        assertTrue(Pawn1P1.ValidateMove(4, 5, congoBoard) == false); // no jump in backward move

        // move 5,3 to 5,4
        congoGame.movePiece(5,3, 5,4);
        // move 6,3 to 5,3?
        assertTrue(Pawn1P1.ValidateMove(5, 3, congoBoard) == true); // yes. 5,3 is null , can move backward
        // move ? 6,3 to 4,3?
        assertTrue(Pawn1P1.ValidateMove(4, 3, congoBoard) == true); // yes, path is clear

        // move 6,3 to 4,1
        congoGame.movePiece(6,3,4,1);
        // 4,2 to 3,0?
        assertTrue(Pawn1P1.ValidateMove(3, 0, congoBoard) == true); // yes, one step orthogonal backward
        // move 4,1 to 6,6
        congoGame.movePiece(4,1, 6,6);
        assertTrue(Pawn1P1.ValidateMove(5, 5, congoBoard) == false); // failed because of obstacles
    }

    @Test
    public void pawnMoveTest(){

        GamePiece[][] congoBoard = congoGame.board;

        PawnPiece Pawn1P1 = (PawnPiece) congoGame.board[1][0];
        PawnPiece Pawn4P1 = (PawnPiece) congoGame.board[1][3];
        PawnPiece Pawn6P1 = (PawnPiece) congoGame.board[1][5];
        PawnPiece Pawn7P1 = (PawnPiece) congoGame.board[1][6];

        // move pawn1p1 from 1,0 to 4,1
        congoGame.movePiece(1,0,6,3);
        // move pawn2p1 from 1,2 to 4,3
        congoGame.movePiece(1,2,4,3);

        // can pawn1 jump from 6,3 to 4,3
        assertTrue(Pawn1P1.ValidateMove(4, 3, congoBoard) == false); // no path is not clear

        // how about jump diagonally form 6,3 to 4,5??
        assertTrue(Pawn1P1.ValidateMove(4, 5, congoBoard) == false); //no path is not clear

        // move player1 pawn to make it super pawn
        congoGame.movePiece(1,5,6,4);
        congoGame.movePiece(6,4,5,5);

        //move from 5,5, to 3,3
        assertTrue(Pawn6P1.ValidateMove(3, 3, congoBoard) == true); // 2 steps diagonally backward

        /* pawn7 has not past the river - can not move backward and two steps forward */
        assertTrue(Pawn7P1.ValidateMove(0, 5, congoBoard) == false); // move backward without passing river ? no
        assertTrue(Pawn7P1.ValidateMove(0, 6, congoBoard) == false);
        assertTrue(Pawn7P1.ValidateMove(3, 4, congoBoard) == false); // can not move 2 steps diagonally forward , only one step

        // move from 1,3 to 4,5
        congoGame.movePiece(1,3,4,5);
        assertTrue(Pawn4P1.ValidateMove(2, 3, congoBoard) == false); // can't move 2 steps back diagonally, cause is not super pawn
        assertTrue(Pawn4P1.ValidateMove(3, 4, congoBoard) == false); // same as above
    }

    @Test
    public void testPawn2SimpleMove(){

        /*Start with initial board and test is Player2 */
        PawnPiece Pawn1P2 = (PawnPiece) congoGame.board[5][0];
        PawnPiece Pawn2P2 = (PawnPiece) congoGame.board[5][1];

        GamePiece[][] congoBoard = congoGame.board;

        // pawn2 can move from (5,0) to (4,0)
        assertTrue(Pawn1P2.ValidateMove(4, 0, congoBoard) == true); // yes, can move forward
        // move from 5,0 to 6,0? no backward yet
        assertTrue(Pawn1P2.ValidateMove(6, 0, congoBoard) == false); // not backward

        // move pawn1 from 1,2 to 4,1
        congoGame.movePiece(1,2, 4,1);
        //pawn2 can move  froward from 5,1 to 4,1 when opponent pawn is in 4,1 ?
        assertTrue(Pawn2P2.ValidateMove(4, 1, congoBoard) == true); //yes , capture

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
        assertTrue(Pawn2P2.ValidateMove(1, 5, congoBoard) == false); // diagonally backward can not capture

        assertTrue(Pawn2P2.ValidateMove(2, 6, congoBoard) == false); // Diagonally 2 step backward works!!
        assertTrue(Pawn2P2.ValidateMove(2, 2, congoBoard) == false); // Diagonally 2 step backward works!!

        // move super pawn player2 to 2,6
        congoGame.movePiece(0,4,2,6);
        assertTrue(Pawn2P2.ValidateMove(3, 5, congoBoard) == true); // can move backward one square diagonally ? yes
        assertTrue(Pawn2P2.ValidateMove(4, 4, congoBoard) == true);
    }

    @Test
    public void pawn2Test(){

        PawnPiece Pawn2P2 = (PawnPiece) congoGame.board[5][2];
        PawnPiece Pawn3P2 = (PawnPiece) congoGame.board[5][3];
        GamePiece[][] congoBoard = congoGame.board;

        // move pawn2 from 5,1 to 0,4 to make it super pawn
        congoGame.movePiece(5,2, 0,4);

        // move pawn2 from 0,4  to 2,6
        congoGame.movePiece(0,4,2,5);
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
        assertTrue(Pawn2P2.ValidateMove(3 , 6 , congoBoard) == true);

        congoGame.movePiece(1,4,2,4);
        assertTrue(Pawn2P2.ValidateMove(0, 6 , congoBoard) == false); //2 steps diagonally forward ? no
        assertTrue(Pawn2P2.ValidateMove(0, 2 , congoBoard) == false); // 2 steps diagonally forward ? no
        assertTrue(Pawn2P2.ValidateMove(1, 3 , congoBoard) == true); // one step diagonally forward? yes
        assertTrue(Pawn2P2.ValidateMove(1, 4 , congoBoard) == true); // one step straight forward
        assertTrue(Pawn2P2.ValidateMove(3, 4 , congoBoard) == true);

        assertTrue(Pawn2P2.ValidateMove(4, 2 , congoBoard) == true); // move backward diagonally 2 steps? yes
        assertTrue(Pawn2P2.ValidateMove(4, 6 , congoBoard) == true);

        congoGame.movePiece(2,4, 2,3);
        assertTrue(Pawn2P2.ValidateMove(4, 5 , congoBoard) == true); // but change position and try again? 2 step back diagonally works

        // move pawn3 from 5,3 to 0,3 - is super pawn
        congoGame.movePiece(5,3, 0,3);

        // move from 0,3 to 0,2?
        assertTrue(Pawn3P2.ValidateMove(0, 2 , congoBoard) == true); //yes side move
        // move from 0,3 to 1,2?
        assertTrue(Pawn3P2.ValidateMove(1, 2, congoBoard) == false); // no- backward move with no capture ability
        // move from 0,3 to 1,4
        assertTrue(Pawn3P2.ValidateMove(1, 4, congoBoard) == true); // yes- it is null
        // // move from 0,3 to 2,1
        assertTrue(Pawn3P2.ValidateMove(2, 1, congoBoard) == false); // path is not clear
    }

    /*
    @Test
    public void forwardMoveTest(){

        // for player1 pawns
        GamePiece pawn1 = congoGame.getGamePiece(1,0);
        assertTrue(pawn1.forwardMove(pawn1.row , 2) == true);
        assertTrue(pawn1.forwardMove(pawn1.row , 3) == true);
        assertTrue(pawn1.forwardMove(pawn1.row , 0) == false);

        // for player 2 pawns
        GamePiece pawn2 = congoGame.getGamePiece(5,0);
        assertTrue(pawn2.forwardMove(pawn2.row , 4) == true);
        assertTrue(pawn2.forwardMove(pawn2.row , 3) == true);
        assertTrue(pawn2.forwardMove(pawn2.row , 6) == false);
    }*/


//    @Test
//    public void pastRiverTest(){
//
//        // for player1
//        GamePiece pawn1 = congoGame.getGamePiece(1,3);
//        assertTrue(pawn1.pastRiver(pawn1.row) == false); /* is on 1,3, has not passed the river*/
//        /*move piece to 3,4 */
//        congoGame.movePiece(1, 3, 3,4); // in the river not passed yet
//        assertTrue(pawn1.pastRiver(pawn1.row) == false);
//        /*move piece to 4,4 */
//        congoGame.movePiece( 3,4, 4,4);
//        assertTrue(pawn1.pastRiver(pawn1.row) == true); // crossed the river
//        /*move piece to 5,6 */
//        congoGame.movePiece( 4,4, 5,6);
//        assertTrue(pawn1.pastRiver(pawn1.row) == true);
//        /*move piece to 2,6 */
//        congoGame.movePiece( 5,6, 2,6);
//        assertTrue(pawn1.pastRiver(pawn1.row) == false);
//
//        // for player 2
//        GamePiece pawn2 = congoGame.getGamePiece(5,3);
//        assertTrue(pawn2.pastRiver(pawn2.row) == false);
//        /* move piece to 4,6*/
//        congoGame.movePiece( 5,3, 4,6);
//        assertTrue(pawn2.pastRiver(pawn2.row) == false);
//        /* move piece to 3,4*/
//        congoGame.movePiece(  4,6, 3,4);
//        assertTrue(pawn2.pastRiver(pawn2.row) == false);
//        /* move piece to 2,5*/
//        congoGame.movePiece(3,4,2,5);
//        assertTrue(pawn2.pastRiver(pawn2.row) == true);
//    }

    @Test
    public void moveOneStepStraightForward(){
        /* move player 1's pawn one step straight or diagonally forward */
        GamePiece pawn1 = congoGame.getGamePiece(1,6);
        GamePiece[][] congoBoard = congoGame.board;

        assertTrue(pawn1.ValidateMove(2, 6, congoBoard) == true); /*move one step straight forward? yes */
        assertTrue(pawn1.ValidateMove(2, 5, congoBoard) == true); /*move one step diagonally forward?yes*/

        assertTrue(pawn1.ValidateMove(0, 6, congoBoard) == false);/*move one step straight backward*/
        assertTrue(pawn1.ValidateMove(0, 5, congoBoard) == false);/*move one step diagonally backward*/
        assertTrue(pawn1.ValidateMove(1, 5, congoBoard) == false);/*move one step side-away*/

        /* move player2's pawn one step straight or diagonally forward */
        GamePiece pawn2 = congoGame.getGamePiece(5,4);
        assertTrue(pawn2.ValidateMove(4, 5, congoBoard) == true); /*move one step diagonally forward? yes*/
        assertTrue(pawn2.ValidateMove(4, 3, congoBoard) == true); /*move one step diagonally forward? yes*/
        assertTrue(pawn2.ValidateMove(3, 4, congoBoard) == false);/*move 2 steps straight forward? no*/
        assertTrue(pawn2.ValidateMove(2, 6, congoBoard) == false);
        assertTrue(pawn2.ValidateMove(5, 5, congoBoard) == false); /*move one step side away? yes*/
    }

    @Test
    public void moveOneStepsStraightBackward() {
        GamePiece[][] congoBoard = congoGame.board;

        /* for player1 - move One step backward*/
        GamePiece pawn1 = congoGame.getGamePiece(1,3);
        assertTrue(pawn1.ValidateMove(3, 3, congoBoard) == false); /*can not move two steps forward*/
        assertTrue(pawn1.ValidateMove(0, 3, congoBoard) == false); /*can not move one steps backward*/

        /* move from 1,3 to river 3,4  */
        congoGame.movePiece(  1,3, 3,4);
        assertTrue(pawn1.ValidateMove(2, 4, congoBoard) == false); /* can not move backward yet*/

        /*Pawn crossed the river, is now on 5,5*/
        congoGame.movePiece(3,4, 5,5);
        assertTrue(pawn1.ValidateMove(6, 5, congoBoard) == true);/*one step straight forward? yes*/
        assertTrue(pawn1.ValidateMove(4, 5, congoBoard) == true);/*one step straight backward? yes*/
        assertTrue(pawn1.ValidateMove(4, 6, congoBoard) == false);/*diagonally one step backward ? no*/
    }

    @Test
    public void moveOneTwoStepsStraightBackwardP2() {
        GamePiece[][] congoBoard = congoGame.board;

        /* for player2 - move One step backward*/
        GamePiece pawn2 = congoGame.getGamePiece(5,3);
        congoGame.movePiece(  5,3,2,3);

        assertTrue(pawn2.ValidateMove(3, 3, congoBoard) == true); /* one step backward ? yes*/
        assertTrue(pawn2.ValidateMove(4, 3, congoBoard) == true); /* two steps backward? yes*/
        assertTrue(pawn2.ValidateMove(1, 3, congoBoard) == true); /* one step forward? yes*/
        assertTrue(pawn2.ValidateMove(1, 4, congoBoard) == true); /* one step diagonally forward? yes*/
        assertTrue(pawn2.ValidateMove(1, 2, congoBoard) == true); /* one step diagonally forward? yes*/
    }

    @Test
    public void moveTwoStepsStraightBackward() {
        GamePiece[][] congoBoard = congoGame.board;

        /* for player1 */
        GamePiece pawn1 = congoGame.getGamePiece(1,3);
        /* move pawn from 1,3 to 5,5*/
        congoGame.movePiece(  1,3, 6,5);
        assertTrue(congoGame.getGamePiece(6,5) instanceof PawnPiece);
        assertTrue(congoGame.getGamePiece(6,5).player ==1 );

        assertTrue(pawn1.ValidateMove(5, 5, congoBoard) == false);/* one steps straight backward? no it is blocked by other pawn */

        congoGame.movePiece(6,5, 5,5);
        assertTrue(pawn1.ValidateMove(4, 5, congoBoard) == true); // move one step backward? yes
        assertTrue(pawn1.ValidateMove(3, 5, congoBoard) == true); // move two step backward? yes
        assertTrue(pawn1.ValidateMove(2, 5, congoBoard) == false); // move backward when you did not cross the river? no

        // check path clear and destination is empty to move from 5,5 to 3,3
        assertTrue(congoGame.getGamePiece(4,4) == null);
        assertTrue(congoGame.getGamePiece(3,3) == null);
        assertTrue(pawn1.ValidateMove(3, 3, congoBoard) == true);// move 2 steps backward diagonally? yes
    }

    @Test
    public void superPawnMoveSide(){

        GamePiece[][] congoBoard = congoGame.board;

        GamePiece myPawn = congoGame.getGamePiece(1,5);
        congoGame.movePiece(1,5, 6, 3);

        assertTrue(congoGame.getGamePiece(6,3) instanceof PawnPiece);

        // move backward from 6,3 to 5,3 and 4,3
        assertTrue(myPawn.ValidateMove(5, 3, congoBoard) == false); // no , can not captured
        assertTrue(myPawn.ValidateMove(4, 3, congoBoard) == false); // no, can not jumped

        // move side away
        assertTrue(congoGame.getGamePiece(6,3).player == 1); // 6,3 is from player1
        assertTrue(congoGame.getGamePiece(6,4).player == 2); // 6,4 is from player 2

        assertTrue(myPawn.ValidateMove(6, 4, congoBoard) == true);
        assertTrue(myPawn.ValidateMove(6, 2, congoBoard) == true);
    }

    @Test
    public void jumpBackward(){

    }

    @Test
    public void testCapture(){

    }
}
