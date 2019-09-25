package Game ;

// import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.Assert.*;
import Game.GamePiece;
//import Game.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;

/*
  This class contains tests for the Congo game app.
 */
//@RunWith(JUnit4.class)
public class GamePieceTest {

    GameBoard gameBoard;
    GamePiece piece;

    // Setup to be done before every test in TestPlan
    @Before
    public void initialize() {
        gameBoard = new GameBoard();
        gameBoard.InitGameBoard();
        piece = new GamePiece(0,0,1);
    }

    @Test
    public void testDefaultConstructor() {
        GamePiece gamePiece = new GamePiece();
        assertTrue(gamePiece.row == 0);
        assertTrue(gamePiece.column == 0);
        assertTrue(gamePiece.player == 0);
    }

    @Test
    public void testConstructor() {
        GamePiece gamePiece = new GamePiece(1, 1, 1);
        assertTrue(gamePiece.row == 1);
        assertTrue(gamePiece.column == 1);
        assertTrue(gamePiece.player == 1);
    }

    @Test
    public void testInRiverWhenGamePieceNotInRiver() {
        assertTrue(!piece.inRiver());
    }

    @Test
    public void testInRiverWhenGamePieceInRiver() {
        piece.row = 3;
        assertTrue(piece.inRiver());
    }

    //test overloaded method?

    @Test
    public void testSquareEmptyOrCapturableWithEnemyPiece() {
    }




}
