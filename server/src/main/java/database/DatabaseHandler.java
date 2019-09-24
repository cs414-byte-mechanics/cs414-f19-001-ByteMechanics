package database;

import java.sql.*;
import webconnection.*;

public class DatabaseHandler {
    private String query;
    private String counterQuery;

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
    
        query = Query.createCheckEmailQuery(action);
        counterQuery = Query.createRegisterUserQuery(action);
        
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            if(!rs.next()){
                ResultSet rs2 = stmt.executeQuery(counterQuery);
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
    
        query = Query.createUnregisterUser(action);
    
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
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
        query = Query.createCheckEmailQuery(action);
        counterQuery = Query.createValidatePasswordQuery(action);
        
        try(Connection con = DriverManager.getConnection("jdbc:mysql://faure/bytemechanics", "jeskea", "831702229"))
        {
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            if(rs.next()){
                ResultSet rs2 = stmt.executeQuery(counterQuery);
                
                if(rs2.next()){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
            
        } catch(Exception e){
            System.out.println(e);
            return false;
        }
        
    }
} 
