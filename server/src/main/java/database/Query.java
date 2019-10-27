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
   
}
