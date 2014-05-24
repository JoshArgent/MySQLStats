package com.joshargent.MySQLStats;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable()
	{ 
		if(!this.getConfig().contains("enabled"))
		{
			this.saveDefaultConfig();
		}
		
	}
	
	public void onDisable()
	{
		
	}

}
