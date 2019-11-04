package Game;

import webconnection.Action;
import database.DatabaseHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.sql.SQLException;
import static org.junit.Assert.*;

/**
Commenting out tests for the sake of Travis CI
*/
public class GameTest {

    Game game;
   
    @Before
    public void initialize() {
        game = new Game();
    }
   
   //@Test
   public void testLoadExistingGame() throws Exception {
        Action action = new Action();
        action.communicationType = "requestMoves";
        action.matchID = "1";
        
        Game game = new Game();
        game.loadExistingGame(action);
   }
}
