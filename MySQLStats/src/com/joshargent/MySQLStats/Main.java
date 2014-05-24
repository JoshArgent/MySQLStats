package com.joshargent.MySQLStats;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	MySQL mysql;
	boolean enabled = false;
	
	public void onEnable()
	{ 
		if(!this.getConfig().isSet("database"))
		{
			this.saveDefaultConfig();
		}
		if(this.getConfig().getBoolean("enabled"))
		{
			mysql = new MySQL(this, getConfig().getString("database.host"), getConfig().getString("database.database"), 
					getConfig().getString("database.username"), getConfig().getString("database.password"));
			checkTables();
			enabled = true;
		}
		else getLogger().info("MySQL Stats not enabled! Edit the config.yml to enable logging of player stats.");
	}
	
	public void onDisable()
	{
		
	}
	
	public void checkTables()
	{
		final String table = getConfig().getString("database.table");
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
			@Override
			public void run()
			{
				if(!mysql.waitForConnection()) return;
				try
				{
					DatabaseMetaData dbm = mysql.getConnection().getMetaData();
					ResultSet tables = dbm.getTables(null, null, table, null);
					if (!tables.next()) // Table not found!
					{
						PreparedStatement stmt = mysql.getConnection().prepareStatement("CREATE TABLE " + table + "(UUID CHAR(32),Name CHAR(16),Play_Time INT,PVP_Kills INT,Mob_Kills INT, Total_Kills INT,PVP_Deaths INT,Monster_Deaths INT,Natural_Deaths INT,Total_Deaths INT,XP INT,Health INT,Hunger INT,Gamemode INT,Distance_Travelled INT,Fish_Caught INT,Items_Crafted INT, PRIMARY KEY(UUID))");
						stmt.executeUpdate();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
    	});		
	}

}
