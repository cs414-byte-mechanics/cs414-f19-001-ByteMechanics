import webconnection.Action;
import database.DatabaseHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class DatabaseHandlerTest {

    DatabaseHandler dbh;
   
    @Before
    public void initialize() {
        dbh = new DatabaseHandler();
    }
   
    //@Test
    public void testRegisterUser() throws Exception {
        Action action = new Action();
        action.communicationType = "registerUser";
        action.userEmail = "dummy@gmail.com";
        action.userName = "dummy";
        action.userPassword = "pass123";
        dbh.registerUser(action);
        
        Action action2 = new Action();
        action2.communicationType = "unregisterUser";
        action2.userName = "dummy";
        dbh.unregisterUser(action2);

    }
    
    //@Test
    public void testRegisterUserTwice() throws Exception {
        //Add first
        Action action = new Action();
        action.communicationType = "registerUser";
        action.userEmail = "dummy@gmail.com";
        action.userName = "dummy";
        action.userPassword = "pass123";
        dbh.registerUser(action);
        
        //Try to add second
        Action action2 = new Action();
        action2.communicationType = "registerUser";
        action2.userEmail = "dummy@gmail.com";
        action2.userName = "dummy";
        action2.userPassword = "pass123";
        dbh.registerUser(action2);
        
        //Remove first
        Action action3 = new Action();
        action3.communicationType = "unregisterUser";
        action3.userName = "dummy";
        dbh.unregisterUser(action3);
    }
    
    //@Test
    public void testUserLogin() throws Exception {
        //Add user
        Action action2 = new Action();
        action2.communicationType = "registerUser";
        action2.userEmail = "hello@world.com";
        action2.userName = "java";
        action2.userPassword = "fortcollins";
        dbh.registerUser(action2);
    
        //Attempt to login
        Action action = new Action();
        action.communicationType = "attemptLogin";
        action.userEmail = "hello@world.com";
        action.userPassword = "fortcollins";
        dbh.attemptLogin(action);
        
        //Remove entry
        Action action3 = new Action();
        action3.communicationType = "unregisterUser";
        action3.userName = "java";
        dbh.unregisterUser(action3);
    }
}
