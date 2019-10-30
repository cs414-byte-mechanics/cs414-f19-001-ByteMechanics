package database;

import java.sql.*;
import webconnection.*;

public class DatabaseHandler {

    private final String DATABASE = "jdbc:mysql://faure/bytemechanics";
    private final String USER = "jeskea";
    private final String PASSWORD = "831702229";

    public void registerUser(Action action) throws Exception {
        Connection con = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        Statement checkCredentials = con.createStatement();
        ResultSet rs = checkCredentials.executeQuery(Query.createCountExistingCredentialsQuery(action));
        if (rs.next() && rs.getInt("total") == 0) {
            Statement registerUser = con.createStatement();
            if(registerUser.executeUpdate(Query.createRegisterUserQuery(action)) != 1 ) throw new Exception("User not registered.");
        } else {
            throw new Exception("User already in system.");
        }
    }

    public void unregisterUser(Action action) throws Exception {
    
        Connection con = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        Statement unregisterUser = con.createStatement();
        int rowsAffected = unregisterUser.executeUpdate(Query.createUnregisterUser(action));

        if (rowsAffected < 1) throw new Exception("No users removed from system.");
    }

    /**
     * @return: username of user logging in
     */
    public String attemptLogin(Action action) throws Exception {
        
        Connection con = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        Statement validateLogin = con.createStatement();
        ResultSet rs = validateLogin.executeQuery(Query.createValidateLoginQuery(action));

        if (!rs.next()) throw new Exception("No user exists with this email and password.");

        return rs.getString("username");
    }
    

    public int addNewGame(Action action, String[][] board) throws Exception {
        Connection con = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        Statement addNewGame = con.createStatement();
        int matchID = addNewGame.executeUpdate(Query.createAddNewGameQuery(action, board), Statement.RETURN_GENERATED_KEYS);
        return matchID;
    }
} 
