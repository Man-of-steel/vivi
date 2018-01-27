/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Man of Steel
 */
public class DAO {
    
    Connection connection;
    Statement statement;
    
    public DAO() {
        connect();        
    }
    
    private void connect(){
        try{
            Class.forName(Constants.DB_DRIVER);
            
            connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
            statement = connection.createStatement();               
        }
        catch( Exception e){e.printStackTrace();}
    }
    
    public ResultSet getData(String sql){
        ResultSet rs = null;
        try{
            rs = statement.executeQuery(sql);
        }
        catch (Exception e){e.printStackTrace();}
        return rs;
    }
    
    public int putData(String sql){
        int rows = 0;
        try{
            rows = statement.executeUpdate(sql);
        }
        catch (Exception e){e.printStackTrace();}
        return rows;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
    
    
}
