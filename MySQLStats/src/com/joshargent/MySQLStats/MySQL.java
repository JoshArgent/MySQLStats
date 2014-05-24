package com.joshargent.MySQLStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

public class MySQL {
	 
    public Connection conn = null;
    public Statement stmt = null;
    public boolean dbConnSuccess = false;
   
    public String strUri = "";
    public String strUser = "";
    public String strPassword = "";
    public String strTbl = "homes";
    
    MySQL(String host, String db, String username, String password, String table)
    {
    	strUri = "jdbc:mysql://" + host + "/" + db;
    	strUser = username;
    	strPassword = password;
    	strTbl = table;
    }
   
    public void dbConnect()
    {
        try {
        	try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
            conn = DriverManager.getConnection(strUri, strUser, strPassword);
            stmt = conn.createStatement();
            Bukkit.getLogger().info("Successfully connected to MySQL database!");
            dbConnSuccess = true;
        } catch (SQLException e) {
        	e.printStackTrace();
            return;
        }
    }
    public ResultSet executeQuery(String s)
    {
    	ResultSet results = null;
        try {
        	results = stmt.executeQuery(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    public void executeUpdate(String s)
    {
        try {
        	stmt.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void checkConnection()
    {
    	try {
			if(this.conn.isClosed())
			{
				dbConnect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
 
}
