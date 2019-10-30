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
        String boardAsString = "";
   
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                boardAsString += board[row][col];
            }
        }
   
        return "INSERT INTO matches (board, p1, p2) VALUES (" + boardAsString + "," + action.playerOneName + "," + action.playerTwoName + ");";
   }
}
