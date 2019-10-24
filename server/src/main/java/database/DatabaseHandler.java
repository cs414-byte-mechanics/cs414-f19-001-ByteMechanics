package database;

import java.sql.*;
import webconnection.*;

public class DatabaseHandler {

    public boolean performDBSearch(Action action) throws SQLException{
   
        if(action.communicationType.equals("registerUser")){
            
            return registerUser(action);
            
        } else if(action.communicationType.equals("unregisterUser")){
            
            return unregisterUser(action);
            
        } else if(action.communicationType.equals("attemptLogin")){
            
            return attemptLogin(action);
            
        } else {
            return true;
        }
    }
    
    /**
    * @return: true if user is registered correctly, false otherwise
    */
    public boolean registerUser(Action action){
        
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt.executeQuery(Query.createCheckEmailQuery(action));
            ResultSet rs2 = stmt2.executeQuery(Query.createCheckUsernameQuery(action));
            
            if(!rs.next() && !rs2.next()){
                Statement stmt3 = con.createStatement();
                ResultSet rs3 = stmt.executeQuery(Query.createRegisterUserQuery(action));
                return true;
            } else {
                return false;
            }
            
        } catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    /**
    * @return: true if user was in database and was removed, or if user wasn't in database
    */
    public boolean unregisterUser(Action action){
    
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt.executeQuery(Query.createUnregisterUser(action));
            return true;
            
        } catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    /**
    * @return: true if user email exists and password matches, false otherwise
    */
    public boolean attemptLogin(Action action){
        
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement validateLogin = con.createStatement();
            ResultSet rs = validateLogin.executeQuery(Query.createValidateLoginQuery(action));
            return (rs.next());

        } catch(Exception e){
            System.out.println(e);
            return false;
        }
        
    }
} 
