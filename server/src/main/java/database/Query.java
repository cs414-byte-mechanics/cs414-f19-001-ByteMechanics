package database;

import webconnection.Action;

public class Query {

    public static String createCheckEmailQuery(Action action){
        return "SELECT * FROM users WHERE email = \"" + action.userEmail + "\";";
    }
    
    public static String createCheckUsernameQuery(Action action){
        return "SELECT * FROM users WHERE username = \"" + action.userName + "\";";
    }

    public static String createRegisterUserQuery(Action action){
        //Still need to check to make sure it doesn't exist first
        return "INSERT INTO users(email, username, password) VALUES(\"" + action.userEmail + "\",\"" + action.userName + "\",\"" + action.userPassword + "\");";
    }
   
    public static String createUnregisterUser(Action action){
        return "DELETE FROM users WHERE username = \"" + action.userName + "\";";
    }
   
    public static boolean updateBoard(){
        return false;
    }
   
    public static boolean updateMatch(){
        return false;
    }
   
    public static boolean beginNewMatch(){
        return false;
    }
   
    public static String createValidateLoginQuery(Action action){
        return "SELECT * FROM users WHERE email = \"" + action.userEmail + "\" AND password =\"" + action.userPassword + "\";";
    }
   
}
