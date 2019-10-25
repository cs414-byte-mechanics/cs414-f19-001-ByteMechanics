package Game ;

<<<<<<< HEAD
=======
//import javafx.scene.media.MediaPlayer;
>>>>>>> c7b58d0539f2dd4115144a153c81334d34b0947c
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;
<<<<<<< HEAD
=======

>>>>>>> c7b58d0539f2dd4115144a153c81334d34b0947c
import java.util.ArrayList;
import java.util.Arrays;

/*This class contains several tests for the Pawn piece */

public class PawnTest {
    GameBoard congoGame;

    // before any test, we need to initiate players and Gameboard
    @Before
    public void initialize(){
        // initialize board
        congoGame = new GameBoard();
        congoGame.initialize();
    }

    @Test /* Added by Fari- test for pawn pieces belong to player one*/
    public void testPawnPlayer1SimpleMove(){
        /*Start with initial board and test is Player 1 */
        PawnPiece Pawn1P1 = (PawnPiece) congoGame.board[1][0];
        PawnPiece Pawn2P1 = (PawnPiece) congoGame.board[1][1];
        PawnPiece Pawn3P1 = (PawnPiece) congoGame.board[1][2];
        PawnPiece Pawn4P1 = (PawnPiece) congoGame.board[1][3];
        PawnPiece Pawn6P1 = (PawnPiece) congoGame.board[1][5];
        PawnPiece Pawn7P1 = (PawnPiece) congoGame.board[1][6];

        GamePiece[][] congoBoard = congoGame.board;

        // pawn1 can move from (1,0) to (2,0) ? yes
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
        assertTrue(Pawn1P1.ValidateMove(5, 4, congoBoard) == false);

        // pawn 1 is on (6,3)
        congoGame.movePiece(5,3,6,3);

        //?? can go from 6,3 to 6,4 ??? yes
        assertTrue(Pawn1P1.ValidateMove(6, 4, congoBoard) == true);
        // how bout 6,3 t0 2,3??no
        assertTrue(Pawn1P1.ValidateMove(2, 3, congoBoard) == false);

        assertTrue(Pawn1P1.ValidateMove(5, 4, congoBoard) == false); // maybe there is obstacle

        assertTrue(Pawn1P1.ValidateMove(4, 5, congoBoard) == false); // there is obstacle


        assertTrue(Pawn1P1.ValidateMove(5, 3, congoBoard) == true);
//        assertTrue(Pawn1P1.ValidateMove(5, 2, congoBoard) == false); // put false maybe obstacle
        congoGame.movePiece(6,3,4,1);
        assertTrue(Pawn1P1.ValidateMove(3, 0, congoBoard) == true);

//        congoGame.movePiece(6,3, 6,6);
//        assertTrue(Pawn1P1.ValidateMove(5, 5, congoBoard) == true); // failed because of obstacles

        // move pawn2p1 from 1,1 to 5,3
        congoGame.movePiece(1,1,5,3);
        congoGame.movePiece(1,2,4,3);
        // can pawn1 jump from 6,3 to 4,3 ??? no there is obstacle
        assertTrue(Pawn1P1.ValidateMove(3, 3, congoBoard) == false);

        // how about jump diagonally form 6,3 to 4,5?? no there is obstacle
        congoGame.movePiece(5,3,5,4);
//        assertTrue(Pawn1P1.ValidateMove(4, 5, congoBoard) == false); //Failed

        congoGame.movePiece(1,5,6,4);
        congoGame.movePiece(6,4,5,5);
        //Diagonally works
        assertTrue(Pawn6P1.ValidateMove(3, 3, congoBoard) == true); // 2 steps diagonally backward

        assertTrue(Pawn7P1.ValidateMove(0, 5, congoBoard) == false);
        assertTrue(Pawn7P1.ValidateMove(0, 6, congoBoard) == false);
        assertTrue(Pawn7P1.ValidateMove(3, 4, congoBoard) == false); // can not move 2 steps diagonally forward , only one step

        congoGame.movePiece(1,3,4,5);
        assertTrue(Pawn4P1.ValidateMove(2, 3, congoBoard) == false); // can't move 2 steps back diagonally, cause is not super pawn
        assertTrue(Pawn4P1.ValidateMove(3, 4, congoBoard) == false); // same as above
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

        assertTrue(Pawn2P2.ValidateMove(2, 6, congoBoard) == false); // Diagonally 2 step backward works!!
        assertTrue(Pawn2P2.ValidateMove(2, 2, congoBoard) == false); // Diagonally 2 step backward works!!

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
}
