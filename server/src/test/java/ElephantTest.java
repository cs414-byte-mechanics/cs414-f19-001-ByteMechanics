package Game ;

//import javafx.scene.media.MediaPlayer;
//import jdk.jfr.StackTrace;
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

/*This class contains several tests for the elephant piece */
public class ElephantTest {
    GameBoard congoGame;
    Player congoPlayer1;
    Player congoPlayer2;

    // before any test, we need to initiate players and Gameboard
    @Before
    public void initialize(){
        // initialize players
        congoPlayer1 = new Player(1);
        congoPlayer2 = new Player(2);

        // initialize board
        congoGame = new GameBoard();
        congoGame.InitGameBoard();
    }

    @Test
    public void testElephantSimpleMove() {
        ElephantPiece elephant1P1 = (ElephantPiece) congoGame.board[0][2];
        ElephantPiece elephant2P1 = (ElephantPiece) congoGame.board[0][4];

        GamePiece[][] congoBoard = congoGame.board;

        // test a blocked move - his pawn is blocking elephant
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == false); /*elephant1 can not move to square occupied by his pawn*/
        // test that elephant can jump two squares up
        assertTrue(elephant1P1.ValidateMove(2, 2, congoBoard) == true); /*but it can jump to 2nd square empty forward*/

        // we moved pawn and then elephant can move one square
        congoGame.movePiece(1, 2, 2, 2); // move pawn (1,2) to (2,2)
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == true); /*now elephant can move to empty square ahead */

        congoGame.movePiece(0, 2, 2, 2); // move elephant two square up
        assertTrue(elephant1P1.ValidateMove(1, 2, congoBoard) == true); // move backward one square
        assertTrue(elephant1P1.ValidateMove(0, 2, congoBoard) == true);// move backward 2 squares

        assertTrue(elephant1P1.ValidateMove(2, 3, congoBoard) == true); // move one square right
        assertTrue(elephant1P1.ValidateMove(2, 4, congoBoard) == true); // move 2 squares right
        assertTrue(elephant1P1.ValidateMove(2, 0, congoBoard) == true); // move 2 squares left

        assertTrue(elephant1P1.ValidateMove(1, 3, congoBoard) == false); // move diagonally is valid for elephant? no

        congoGame.movePiece(0,6,2,6);
        assertTrue(elephant2P1.ValidateMove(0, 6, congoBoard) == true); // move 2 steps right OK

        congoGame.movePiece(0,4,2,4);
        assertTrue(elephant2P1.ValidateMove(0, 4, congoBoard) == true); //  move 2 steps down OK

        congoGame.movePiece(2,2,3,2);
        assertTrue(elephant2P1.ValidateMove(2, 2, congoBoard) == true);  //move 2 steps left OK
    }

    @Test
    public void testElephantBlockedMove() { // only if its teammate pieces occupied a square it get blocked
        ElephantPiece elephant2P1 = (ElephantPiece) congoGame.board[0][4];
        GamePiece[][] congoBoard = congoGame.board;

        assertTrue(elephant2P1.ValidateMove(2, 4, congoBoard) == true); /* elephant can move 2 square up*/
        assertTrue(elephant2P1.ValidateMove(1, 4, congoBoard) == false); // it is blocked by pawn
        assertTrue(elephant2P1.ValidateMove(0, 2, congoBoard) == false); // The other elephant blocked it
    }

    @Test
    public void testElephantPerformMoveWithCapture(){
        ElephantPiece elephant1P1 = (ElephantPiece) congoGame.board[0][2];
        GamePiece[][] congoBoard = congoGame.board;

        congoGame.movePiece(6,0,3,4); // move an opponent's piece to (3,4) in the river
        congoGame.movePiece(0,2,3,3); // move elephant to 3,3
        assertTrue(elephant1P1.ValidateMove(3, 4, congoBoard) == true); // move one step right to capture opponent's piece

        congoGame.movePiece(3,4,2,3); // move opponent's piece to 2,3
        assertTrue(elephant1P1.ValidateMove(2, 3, congoBoard) == true); // try to capture opponent piece

        congoGame.movePiece(2,3,1,3); // move opponent's piece to 1,3
        assertTrue(elephant1P1.ValidateMove(1,3, congoBoard) == true); // capture from 3,3 to 1,1??

        congoGame.movePiece(1,4, 1,3); // move teammate pawn on 1,3
        assertTrue(elephant1P1.ValidateMove(1, 3, congoBoard) == false); // try to capture teammate piece ? no
    }

    @Test /* Test for elephant piece player2 moves */
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
}
