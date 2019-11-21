package database;

import webconnection.Action;

public class Query {
    
    public static String createCountExistingCredentialsQuery(Action action){
        return "SELECT COUNT(*) AS total FROM users WHERE username= \"" + action.userName + "\" OR email=\"" + action.userEmail + "\";";
    }

    public static String createRegisterUserQuery(Action action){
        //Still need to check to make sure it doesn't exist first
        return "INSERT INTO users(email, username, password) VALUES(\"" + action.userEmail + "\",\"" + action.userName + "\",\"" + action.userPassword + "\");";
    }
   
    public static String createUnregisterUser(Action action){
        return "DELETE FROM users WHERE username = \"" + action.userName + "\";";
    }

    public static String createValidateLoginQuery(Action action){
        return "SELECT * FROM users WHERE email = \"" + action.userEmail + "\" AND password =\"" + action.userPassword + "\";";
    }
   
   public static String createAddNewGameQuery(Action action, String[][] board){
   
        return "INSERT INTO matches (board, next_turn, p1, p2, start) VALUES (\"" + boardToString(board) + "\", \"" + action.playerOneName
                + action.playerOneName + "\", \"" + action.playerTwoName + "\", CURRENT_TIMESTAMP);";
   }
   
   public static String createRetrieveGameQuery(Action action){
        return "SELECT * FROM matches WHERE match_id=\"" + action.matchID + "\";";
   }
   
   public static String createUpdateGameStateQuery(int matchID, String[][] board){
        return "UPDATE matches SET board = \"" + boardToString(board) + "\" WHERE match_id = " + matchID + ";";
   }

   public static String createSearchUserQuery(Action action) {
        return "SELECT * FROM users WHERE username LIKE \"%" + action.userName + "%\" AND username != \"" + action.playerName +"\";";
   }

   public static String createGetCurrentInvitationsOrTimesQuery(String colName, String userName) {
        return "SELECT " + colName + " from users where username = \"" + userName + "\";";
   }

   public static String createUpdateInvitationsOrTimesQuery(String colName, String updatedInvitationsOrTimes, String userName) {
        return "UPDATE users SET " + colName + " = \"" + updatedInvitationsOrTimes + "\" WHERE username = \"" + userName + "\";";
   }

  
   public static String boardToString(String[][] board){
        String boardAsString = "";
   
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                boardAsString += board[row][col];
            }
        }
        return boardAsString;
   }
}
