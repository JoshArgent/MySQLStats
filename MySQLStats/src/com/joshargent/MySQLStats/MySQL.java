package com.joshargent.MySQLStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL {
	 
    private Connection conn = null;
    private Statement stmt = null;
   
    private String strUri = "";
    private String strUser = "";
    private String strPassword = "";
    private JavaPlugin plugin;
    public boolean connected = false;
    public boolean failed = false;
    
    MySQL(JavaPlugin plugin, String host, String db, String username, String password)
    {
    	strUri = "jdbc:mysql://" + host + "/" + db;
    	strUser = username;
    	strPassword = password;
    	this.plugin = plugin;
    	dbConnect();    	
    }
   
    public void dbConnect()
    {
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run()
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
		            connected = true;
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        	failed = true;
		            return;
		        }
			}
    	});
    }
    
    public boolean waitForConnection()
    {
    	while(!failed && !connected)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(failed) return false;
		return true;
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
    
    public Connection getConnection()
    {
    	return conn;
    }
    
    public void executeUpdateAsync(final String s)
    {
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run()
			{
				try {
		        	stmt.executeUpdate(s);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
    	});
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
