package Game ;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GiraffeTest {
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
        congoGame.initialize();
    }

    @Test /* Added By Fari -- Test for Giraffe piece valid moves */
    public void testGiraffeP1SimpleMove(){
        /*Start with initial board and test is Player 1 giraffe can move from (0,0) to (2,0) */

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

}
