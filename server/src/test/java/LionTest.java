package Game ;

import Game.GamePiece;
import Game.GiraffePiece;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import javax.annotation.processing.SupportedAnnotationTypes;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LionTest {

    GameBoard congoGame;

    // before any test, we need to initiate players and Gameboard
    @Before
    public void initialize(){

        // initialize board
        congoGame = new GameBoard();
        congoGame.initialize();
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


}
