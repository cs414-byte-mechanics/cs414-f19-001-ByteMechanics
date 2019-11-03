package database;

import java.sql.*;
import java.util.ArrayList;

import webconnection.*;
import game.*;

//NOTE: see the project README for being able to connect to the database from off-campus or when not on a CS lab machine

public class DatabaseHandler {

    private String database;
    private final String USER = "jeskea";
    private final String PASSWORD = "831702229";

    public DatabaseHandler() {
        String tunnel = System.getenv("TUNNEL");
        if (tunnel != null && tunnel.equals("true")) {
            database = "jdbc:mysql://localhost:56247/bytemechanics";
        }
        else {
            database = "jdbc:mysql://faure/bytemechanics";
        }
    }

    public void registerUser(Action action) throws Exception {
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
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
    
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
        Statement unregisterUser = con.createStatement();
        int rowsAffected = unregisterUser.executeUpdate(Query.createUnregisterUser(action));

        if (rowsAffected < 1) throw new Exception("No users removed from system.");
    }

    /**
     * @return username of user logging in
     */
    public String attemptLogin(Action action) throws Exception {
        
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
        Statement validateLogin = con.createStatement();
        ResultSet rs = validateLogin.executeQuery(Query.createValidateLoginQuery(action));

        if (!rs.next()) throw new Exception("No user exists with this email and password.");

        return rs.getString("username");
    }

    public String searchUser(Action action) throws Exception {

        ResultSet rs = null;

        try {
            Connection con = DriverManager.getConnection(database, USER, PASSWORD);
            Statement search = con.createStatement();
            rs = search.executeQuery(Query.createSearchUserQuery(action));
        } catch(Exception e) {return "user not found";}

        if (!rs.next()) {
            return "user not found";
        }
        else {
            return "user found";
        }
    }

    /**
    @return matchID of game
    */
    public int addNewGame(Action action, String[][] board) throws Exception {
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
        Statement addNewGame = con.createStatement();
        int matchID = addNewGame.executeUpdate(Query.createAddNewGameQuery(action, board), Statement.RETURN_GENERATED_KEYS);
        return matchID;
    }
    
    /**
    @return board state of game
    */
    public String[][] retrieveGameInfo(Action action) throws Exception {
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
        Statement gameInfo = con.createStatement();
        ResultSet results = gameInfo.executeQuery(Query.createRetrieveGameQuery(action));
        
        if (!results.next()) throw new Exception("No game exists with this match ID.");
        
        String boardAsString = results.getString("board");
        String[][] board = new String[GameBoard.NUM_ROWS][GameBoard.NUM_COLUMNS];
        
        int index = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = Character.toString(boardAsString.charAt(index));
                index++;
            }
        }
        return board;
    }
    
    public void saveGameState(int matchID, String[][] board) throws Exception {
        Connection con = DriverManager.getConnection(database, USER, PASSWORD);
        Statement saveGame = con.createStatement();
        int rowsAffected = saveGame.executeUpdate(Query.createUpdateGameStateQuery(matchID, board));
        
        if (rowsAffected < 1) throw new Exception("Game state was not saved in database.");
    }

    public void sendGameInvitation(Action action) throws Exception {
        ResultSet rs = null;
        String currInvs = "";
        String updatedInvitations = "";
        String currInvTimes = "";
        String updatedInvitationTimes = "";

        try {
            Connection con = DriverManager.getConnection(database, USER, PASSWORD);
            Statement currentInvitations = con.createStatement();
            rs = currentInvitations.executeQuery(Query.createGetCurrentInvitationsQuery(action));

            if (!rs.next()) {
                throw new Exception();
            }
            else {
                currInvs = rs.getString(1);
            }

            updatedInvitations = currInvs + "," + action.invitationFrom;
            Statement updateInvitations = con.createStatement();
            rs = updateInvitations.executeQuery(Query.createUpdateInvitationsQuery(updatedInvitations, action));

            Statement currentInvitationTimes = con.createStatement();
            rs = currentInvitationTimes.executeQuery(Query.createGetCurrentInvitationTimesQuery(action));

            if (!rs.next()) {
                throw new Exception();
            }
            else {
                currInvTimes = rs.getString(1);
            }

            updatedInvitationTimes = currInvTimes + "," + System.currentTimeMillis();
            Statement updateInvitationTimes = con.createStatement();
            rs = updateInvitationTimes.executeQuery(Query.createUpdateInvitationTimesQuery(updatedInvitationTimes, action));


        } catch(Exception e) {throw new Exception();}
    }

    public ArrayList<String> loadInvitations(Action action) throws Exception {
        ResultSet rs = null;
        String invitations = "";
        String invitationTimes = "";

        try {
            Connection con = DriverManager.getConnection(database, USER, PASSWORD);
            Statement getInvitations = con.createStatement();
            rs = getInvitations.executeQuery(Query.createGetCurrentInvitationsQuery(action));

            if (!rs.next()) {
                throw new Exception();
            }

            invitations = rs.getString(1);
            Statement getInvitationTimes = con.createStatement();
            rs = getInvitationTimes.executeQuery(Query.createGetCurrentInvitationTimesQuery(action));

            if (!rs.next()) {
                throw new Exception();
            }

            invitationTimes = rs.getString(1);
        } catch(Exception e) {throw new Exception();}

        ArrayList<String> invitationsAndTimes = new ArrayList<>();
        invitationsAndTimes.add(invitations);
        invitationsAndTimes.add(invitationTimes);

        return invitationsAndTimes;
    }

} 
