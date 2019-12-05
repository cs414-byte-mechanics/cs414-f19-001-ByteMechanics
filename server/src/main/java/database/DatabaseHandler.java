package database;

import java.sql.*;
import webconnection.*;
import Game.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.Long;
import java.util.Date;
import java.lang.String;
//NOTE: see the project README for being able to connect to the database from off-campus or when not on a CS lab machine

public class DatabaseHandler  {

    private String database;
    private final String USER = "jeskea";
    private final String PASSWORD = "831702229";

    private Connection con;


    public DatabaseHandler() {
        String tunnel = System.getenv("TUNNEL");
        if (tunnel != null) {
            System.out.println("Using tunnel to dB");
            System.out.println(tunnel.toString());
            database = "jdbc:mysql://localhost:" + tunnel.trim() + "/bytemechanics";
        }
        else {
            database = "jdbc:mysql://faure/bytemechanics";
        }
        try {
            con = DriverManager.getConnection(database, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerUser(Action action) throws Exception {
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
        Statement unregisterUser = con.createStatement();
        int rowsAffected = unregisterUser.executeUpdate(Query.createUnregisterUser(action));

        if (rowsAffected < 1) throw new Exception("No users removed from system.");
    }

    /**
     * @return username of user logging in
     */
    public String attemptLogin(Action action) throws Exception {
        Statement validateLogin = con.createStatement();
        ResultSet rs = validateLogin.executeQuery(Query.createValidateLoginQuery(action));

        if (!rs.next()) throw new Exception("No user exists with this email and password.");
        return rs.getString("username");
    }

    public String[] searchUser(Action action) throws Exception {
        Statement search = con.createStatement();
        ResultSet rs = search.executeQuery(Query.createSearchUserQuery(action));

        ArrayList<String> users = new ArrayList<String>();
        while (rs.next()) {
            users.add(rs.getString("username"));
        }
        return Arrays.asList(users.toArray()).toArray(new String[0]);
    }

    public String[] searchGames(Action action) throws Exception {
        String dateString = "";
        Statement search = con.createStatement();
        ResultSet rs = search.executeQuery(Query.createSearchGamesQuery(action));

        ArrayList<String> matches = new ArrayList<String>();
        while (rs.next()) {
            String player1 = rs.getString("p1");
            String player2 = rs.getString("p2");
            String opponent = (player1.compareTo(action.userName) == 0) ? player2 : player1;
            String status = rs.getString("status");
            Timestamp timestamp = rs.getTimestamp("start");
            if (timestamp != null){
                Date date = new java.util.Date(timestamp.getTime());
                LocalDate ldate = LocalDate.from(date.toInstant().atZone(ZoneOffset.UTC));
                dateString = DateTimeFormatter.ISO_DATE.format(ldate);
            }

            matches.add(Integer.toString(rs.getInt("match_id")) + "," + opponent + "," + status + "," + dateString);
        }
        return Arrays.asList(matches.toArray()).toArray(new String[0]);
    }

    /**
     @return matchID of game
     */
    public int addNewGame(Action action, String[][] board) throws Exception {
        Statement addNewGame = con.createStatement();
        int matchID = addNewGame.executeUpdate(Query.createAddNewGameQuery(action, board), Statement.RETURN_GENERATED_KEYS);
        return matchID;
    }

    /**
     @return board state of game
     */
    public String[][] retrieveGameInfo(Action action) throws Exception {
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

    /**
     @return player whose move is next
     */
    public String retrieveActivePlayerInfo(Action action) throws Exception {
        Statement gameInfo = con.createStatement();
        ResultSet results = gameInfo.executeQuery(Query.createRetrieveGameQuery(action));

        if (!results.next()) throw new Exception("No game exists with this match ID.");

        String activePlayer = results.getString("next_turn");
        return activePlayer;
    }

    /**
     @return winner
     */
    public String retrieveWinnerInfo(Action action) throws Exception {
        Statement gameInfo = con.createStatement();
        ResultSet results = gameInfo.executeQuery(Query.createRetrieveGameQuery(action));

        if (!results.next()) throw new Exception("No game exists with this match ID.");

        String winner = results.getString("winner");
        return winner;
    }

    public void saveGameState(int matchID, String nextPlayer, String[][] board, String winner) throws Exception {
        Statement saveGame = con.createStatement();
        int rowsAffected = saveGame.executeUpdate(Query.createUpdateGameStateQuery(matchID, board));

        if (rowsAffected < 1) throw new Exception("Game state was not saved in database.");

        rowsAffected = saveGame.executeUpdate(Query.createUpdateGameNextTurnQuery(matchID,nextPlayer));
        if (rowsAffected < 1) throw new Exception("Next player was not saved in database.");

        rowsAffected = saveGame.executeUpdate(Query.createUpdateGameWinnerQuery(matchID,winner));
        if (rowsAffected < 1) throw new Exception("Winner was not saved in database.");

    }

    public void sendGameInvitation(Action action) throws Exception {
        if(action.invitationFrom.equals(action.invitationTo)) throw new Exception("User cannot invite self");

        String invColTo = "invitations_sent_to";
        String invColFrom = "received_invitations_from";
        String invColTimeTo = "invitations_sent_times";
        String invColTimeFrom = "invitations_received_times";

        String currentInvitationsTo = getCurrentInvitationsOrTimes(invColTo, action.invitationFrom);
        setInvitationsOrTimes(invColTo, currentInvitationsTo, action.invitationFrom, action.invitationTo);

        String currentInvitationsFrom = getCurrentInvitationsOrTimes(invColFrom, action.invitationTo);
        setInvitationsOrTimes(invColFrom, currentInvitationsFrom, action.invitationTo, action.invitationFrom);

        String currentTime = Long.toString(System.currentTimeMillis());

        String currentInvitationsTimesTo = getCurrentInvitationsOrTimes(invColTimeTo, action.invitationFrom);
        setInvitationsOrTimes(invColTimeTo, currentInvitationsTimesTo, action.invitationFrom, currentTime);

        String currentInvitationsTimesFrom = getCurrentInvitationsOrTimes(invColTimeFrom, action.invitationTo);
        setInvitationsOrTimes(invColTimeFrom, currentInvitationsTimesFrom, action.invitationTo, currentTime);

    }

    public String getCurrentInvitationsOrTimes(String colName, String invitationsOf) throws Exception {

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

    public void setInvitationsOrTimes(String colName, String currentInvitationsOrTimes, String addToInvitationsListOf, String invitedOrInviting) throws Exception {
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
            throw new Exception("Duplicate invitation");
        }

        Statement updateCurrentInvsOrTimes = con.createStatement();
        ResultSet rs = updateCurrentInvsOrTimes.executeQuery(Query.createUpdateInvitationsOrTimesQuery(colName, updated, addToInvitationsListOf));

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

    public ArrayList<List<String>> getInvitationLists(Action action) throws Exception {

        ArrayList<List<String>> invitationLists = new ArrayList<>();

        List<String> sentToNames = new ArrayList<>();
        List<String> sentToTimes = new ArrayList<>();
        List<String> receivedFromNames = new ArrayList<>();
        List<String> receivedFromTimes = new ArrayList<>();

        String invColTo = "invitations_sent_to";
        String invColFrom = "received_invitations_from";
        String invColTimeTo = "invitations_sent_times";
        String invColTimeFrom = "invitations_received_times";

        try {
            String currentInvitationsTo = getCurrentInvitationsOrTimes(invColTo, action.userName);
            sentToNames = Arrays.asList(currentInvitationsTo.split(","));
        }catch(Exception e) {sentToNames.add("EMPTY");}

        try {
            String currentInvitationsFrom = getCurrentInvitationsOrTimes(invColFrom, action.userName);
            receivedFromNames = Arrays.asList(currentInvitationsFrom.split(","));
        }catch(Exception e) {receivedFromNames.add("EMPTY");}

        try {
            String currentInvitationsTimesTo = getCurrentInvitationsOrTimes(invColTimeTo, action.userName);
            sentToTimes = Arrays.asList(currentInvitationsTimesTo.split(","));
        }catch(Exception e) {sentToTimes.add("EMPTY");}

        try {
            String currentInvitationsTimesFrom = getCurrentInvitationsOrTimes(invColTimeFrom, action.userName);
            receivedFromTimes = Arrays.asList(currentInvitationsTimesFrom.split(","));
        }catch(Exception e) {receivedFromTimes.add("EMPTY");}

        invitationLists.add(sentToNames);
        invitationLists.add(sentToTimes);
        invitationLists.add(receivedFromNames);
        invitationLists.add(receivedFromTimes);

        return invitationLists;
    }

    public String abandonActiveGame(Action action) throws Exception {
        //Retrieve game info to get the player that didn't abandon
        Statement retrieveGame = con.createStatement();
        ResultSet results = retrieveGame.executeQuery(Query.createRetrieveGameQuery(action));

        if (!results.next()) throw new Exception("No game exists with this match ID.");

        String playerOne = results.getString("p1");
        String playerTwo = results.getString("p2");
        String winner = "";

        if(playerOne.equals(action.playerQuitting)){
            winner = playerTwo;
        } else {
            winner = playerOne;
        }

        //Set the player to be the winner and flag the game as abandoned
        Statement abandonGame = con.createStatement();
        abandonGame.executeQuery(Query.createAbandonGameQuery(action.matchID, winner));

        return winner;
    }

    public void removeInvitation(Action action) throws Exception {

        String invColTo = "invitations_sent_to";
        String invColFrom = "received_invitations_from";
        String invColTimeTo = "invitations_sent_times";
        String invColTimeFrom = "invitations_received_times";

        try {
            String currentInvitationsTo = getCurrentInvitationsOrTimes(invColTo, action.invitationFrom);

            List<String> currentInvitationsToList = Arrays.asList(currentInvitationsTo.split(","));
            int timeIndexInvTo = currentInvitationsToList.indexOf(action.userName);

            deleteInvitationOrTime(invColTo, currentInvitationsTo, action.invitationFrom, action.userName, false, -1);

            String currentInvitationsFrom = getCurrentInvitationsOrTimes(invColFrom, action.userName);

            List<String> currentInvitationsFromList = Arrays.asList(currentInvitationsFrom.split(","));
            int timeIndexInvFrom = currentInvitationsFromList.indexOf(action.invitationFrom);

            deleteInvitationOrTime(invColFrom, currentInvitationsFrom, action.userName, action.invitationFrom, false, -1);

            String currentInvitationsTimesTo = getCurrentInvitationsOrTimes(invColTimeTo, action.invitationFrom);
            deleteInvitationOrTime(invColTimeTo, currentInvitationsTimesTo, action.invitationFrom, action.userName, true, timeIndexInvTo);

            String currentInvitationsTimesFrom = getCurrentInvitationsOrTimes(invColTimeFrom, action.userName);
            deleteInvitationOrTime(invColTimeFrom, currentInvitationsTimesFrom, action.userName, action.invitationFrom, true, timeIndexInvFrom);

        } catch(Exception e) {
            System.out.println(e.toString());
            throw e;
        }

    }

    public void deleteInvitationOrTime(String colName, String currentInvitationsOrTimes, String removeFromInvitationsListOf, String invitingOrInvited, boolean isTime, int timeIndex) throws Exception {
        try {
            List<String> updatedListTemp = Arrays.asList(currentInvitationsOrTimes.split(","));
            ArrayList<String> updatedList = new ArrayList<>(updatedListTemp);

            if (isTime) {
                updatedList.remove(timeIndex);
            } else {
                updatedList.remove(updatedList.indexOf(invitingOrInvited));
            }

            String updated = String.join(",", updatedList);

            Statement updateCurrentInvsOrTimes = con.createStatement();

            if (updatedList.size() == 0) {
                ResultSet rs = updateCurrentInvsOrTimes.executeQuery(Query.createUpdateNullInvitationsOrTimesQuery(colName, removeFromInvitationsListOf));
            }
            else {
                ResultSet rs = updateCurrentInvsOrTimes.executeQuery(Query.createUpdateInvitationsOrTimesQuery(colName, updated, removeFromInvitationsListOf));
            }
        } catch(Exception e) {throw e;}

    }
    
    public ResultSet getGameInfo(String matchID) throws Exception {
        Statement gameInfo = con.createStatement();
        return gameInfo.executeQuery(Query.createGetGameInfoQuery(matchID));
    }

}
