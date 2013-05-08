/* Copyright 2012
 * All rights reserved. Plugin created by PerwinCZ.
 * */

package me.PerwinCZ.MurdersList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.*;

public class MurdersList extends JavaPlugin {
	public Logger logger = Logger.getLogger("Minecraft");

	public String SqlHostname;
	public String SqlPort;
	public String SqlUsername;
	public String SqlPassword;
   	public String SqlDatabase;
   	public static String TablePrefix;
   	
	public void onEnable() {
		logger.info("[MurdersList] Plugin has been enabled!");
		
		File config = new File(getDataFolder(), "config.yml");
		File dir = getDataFolder();
		
		if(!dir.exists()) { 
	  	    boolean success = dir.mkdirs();
	    	if(!success) {
		    	logger.info(ChatColor.RED + "[ERROR] [MurdersList] Couldn't create directory MurdersList!");	
		    } 
		}
		
		if(!config.exists()) {
		      logger.info("[MurdersList] File config.yml doesn't exist, creating new one.");
			  Writer output = null;
			  String text = "SqlHostname: '127.0.0.1'\n" +
				  	"SqlPort: '3306'\n" +
			  		"SqlUsername: 'root'\n" +
			  		"SqlPassword: '12345'\n" +
			  		"SqlDatabase: 'minecraft'\n" +
			  		"TablePrefix: 'mc_'";
	    	  try {
		    	  output = new BufferedWriter(new FileWriter(config));
	    		  output.write(text);
	 	    	  output.close();
	          } catch (IOException ex) {
	        	  logger.info(ChatColor.RED + "[ERROR] [MurdersList] Couldn't create file config.yml!");
			  } 
		} else {
			SqlHostname = getConfig().getString("SqlHostname");
			SqlPort = getConfig().getString("SqlPort");
			SqlUsername = getConfig().getString("SqlUsername");
	   		SqlPassword = getConfig().getString("SqlPassword");
	       	SqlDatabase = getConfig().getString("SqlDatabase");
	       	TablePrefix = getConfig().getString("TablePrefix");
	    	
	       	Connection conn = null;
	       	Statement stat = null;
	       	ResultSet res = null;
	       	DatabaseMetaData meta = null;
	        try {
    	        Class.forName("com.mysql.jdbc.Driver").newInstance();
    	        conn = DriverManager.getConnection("jdbc:mysql://"+ SqlHostname +":"+ SqlPort +"/"+ SqlDatabase, SqlUsername, SqlPassword);
    	        meta = conn.getMetaData();
    	        stat = conn.createStatement();
    	        res = meta.getTables(null, null, "MurdersList", null);
	        	logger.info("[MurdersList] Successfully connected to MySQL database.");
    	        if (!res.next()) {
    	        	logger.info("[MurdersList] Creating new table in database.");
    	        	stat.executeUpdate("CREATE TABLE "+ TablePrefix +"MurdersList (world TEXT, killer TEXT, killed TEXT, weapon INT, armor TEXT, health INT, killerpot TEXT, killedpot TEXT, lasthit INT, time DATETIME, id INT NOT NULL AUTO_INCREMENT PRIMARY KEY)");
    	        }
   	        } catch (Exception e) {
   	        	logger.info(ChatColor.RED + "[ERROR] [MurdersList] Couldn't connect to MySQL database!");
	        } finally {
	            if (conn != null) { try { conn.close(); } catch (SQLException ignore) {} }
	        }
	       	
		}
		
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new MurdersListDeath(this), this);
	}
 
	public void onDisable(){
		logger.info("[MurdersList] Plugin has been disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("murderslist")){
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "[MurdersList]\n" +
						"Plugin version: 1.0\n" +
						"Plugin created by PerwinCZ. All rights reserved.");
			} else {
				sender.sendMessage(ChatColor.RED + "Command usage: /murderslist");
			}
			// doSomething
			return true;
		} 
		return false; 
	}
	
}
