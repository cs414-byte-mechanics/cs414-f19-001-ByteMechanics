package database;

import java.sql.*;
import webconnection.*;
import Game.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Long;

//NOTE: see the project README for being able to connect to the database from off-campus or when not on a CS lab machine

public class DatabaseHandler {

    private String database;
    private final String USER = "jeskea";
    private final String PASSWORD = "831702229";

    public DatabaseHandler() {
        String tunnel = System.getenv("TUNNEL");
        if (tunnel != null) {
            database = "jdbc:mysql://localhost:" + tunnel.trim() + "/bytemechanics";
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

        System.out.println(action);

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
        try {
            Connection con = DriverManager.getConnection(database, USER, PASSWORD);
            String invColTo = "invitations_sent_to";
            String invColFrom = "received_invitations_from";
            String invColTimeTo = "invitations_sent_times";
            String invColTimeFrom = "invitations_received_times";

            String currentInvitationsTo = getCurrentInvitationsOrTimes(con, invColTo, action.invitationFrom);
            setInvitationsOrTimes(con, invColTo, currentInvitationsTo, action.invitationFrom, action.invitationTo);

            String currentInvitationsFrom = getCurrentInvitationsOrTimes(con, invColFrom, action.invitationTo);
            setInvitationsOrTimes(con, invColFrom, currentInvitationsFrom, action.invitationTo, action.invitationFrom);

            String currentTime = Long.toString(System.currentTimeMillis());

            String currentInvitationsTimesTo = getCurrentInvitationsOrTimes(con, invColTimeTo, action.invitationFrom);
            setInvitationsOrTimes(con, invColTimeTo, currentInvitationsTimesTo, action.invitationFrom, currentTime);

            String currentInvitationsTimesFrom = getCurrentInvitationsOrTimes(con, invColTimeFrom, action.invitationTo);
            setInvitationsOrTimes(con, invColTimeFrom, currentInvitationsTimesFrom, action.invitationTo, currentTime);

        } catch (Exception e) {
            throw e;
        }

    }

    public String getCurrentInvitationsOrTimes(Connection con, String colName, String invitationsOf) throws Exception {

        String current = "";

        try {
            Statement currentInvsOrTimes = con.createStatement();
            ResultSet rs = currentInvsOrTimes.executeQuery(Query.createGetCurrentInvitationsOrTimesQuery(colName, invitationsOf));

            if (!rs.next()) {
                throw new Exception("rs.next() returned false");
            }
            else {
                current = rs.getString(1);
            }

        } catch (Exception e) {
            throw e;
        }

        return current;

    }

    public void setInvitationsOrTimes(Connection con, String colName, String currentInvitationsOrTimes, String addToInvitationsListOf, String invitedOrInviting) throws Exception {
        String updated = "";

        if (!duplicateInvitation(currentInvitationsOrTimes, invitedOrInviting)) {
            if (currentInvitationsOrTimes == null) {
                updated = invitedOrInviting;
            }
            else {
                updated = currentInvitationsOrTimes + "," + invitedOrInviting;
            }
        }
        else {
            throw new Exception("duplicate invitation");
        }

        try {
            Statement updateCurrentInvsOrTimes = con.createStatement();
            ResultSet rs = updateCurrentInvsOrTimes.executeQuery(Query.createUpdateInvitationsOrTimesQuery(colName, updated, addToInvitationsListOf));

        } catch(Exception e) {
            throw e;
        }
    }

    public boolean duplicateInvitation(String currentInvitations, String newInvitation) {

        if (currentInvitations != null) {
            ArrayList<String> currentInvs = new ArrayList<>(Arrays.asList(currentInvitations.split(",")));
            if (currentInvs.contains(newInvitation)) {
                return true;
            }
        }
        return false;
    }
} 
