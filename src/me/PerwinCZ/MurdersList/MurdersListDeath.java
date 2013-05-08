/* Copyright 2012
 * All rights reserved. Plugin created by PerwinCZ.
 * */

package me.PerwinCZ.MurdersList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MurdersListDeath implements Listener {
    public MurdersList plugin;
    
    public MurdersListDeath(MurdersList instance) {
            plugin = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
    	Player player = (Player) event.getEntity();
    	if (player.getKiller() instanceof Player) {
        	// event.getEntity().sendMessage(ChatColor.RED + "Hrac " + event.getEntity().getName() +" byl zabit hracem "+ player.getKiller().getName() +", pouzita zbran: "+ player.getKiller().getItemInHand().getTypeId() +"!");
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
    		Connection conn = null;
	       	Statement stat = null;
	       	int helmet = 0;
	       	int chestplate = 0;
	       	int leggings = 0;
	       	int boots = 0;
	       	if(player.getKiller().getInventory().getHelmet() != null) { helmet = player.getKiller().getInventory().getHelmet().getTypeId(); }
	       	if(player.getKiller().getInventory().getChestplate() != null) { chestplate = player.getKiller().getInventory().getChestplate().getTypeId(); }
	       	if(player.getKiller().getInventory().getLeggings() != null) { leggings = player.getKiller().getInventory().getLeggings().getTypeId(); }
	       	if(player.getKiller().getInventory().getBoots() != null) { boots = player.getKiller().getInventory().getBoots().getTypeId(); }
	       	Collection<PotionEffect> objeffskiller = player.getKiller().getActivePotionEffects();
	       	PotionEffect conveffskiller[] = (PotionEffect[])objeffskiller.toArray(new PotionEffect[0]);
	       	String effstypeskiller = "";
	       	for (int i = 0, n = conveffskiller.length; i < n; i++) {
	       	  effstypeskiller += conveffskiller[i].getType() +", ";
	       	}
	       	Collection<PotionEffect> objeffskilled = event.getEntity().getActivePotionEffects();
	       	PotionEffect conveffskilled[] = (PotionEffect[])objeffskilled.toArray(new PotionEffect[0]);
	       	String effstypeskilled = "";
	       	for (int i = 0, n = conveffskilled.length; i < n; i++) {
	       	  effstypeskilled += conveffskilled[i].getType() +", ";
	       	}
	        try {
    	        Class.forName("com.mysql.jdbc.Driver").newInstance();
    	        conn = DriverManager.getConnection("jdbc:mysql://"+ plugin.SqlHostname +":"+ plugin.SqlPort +"/"+ plugin.SqlDatabase, plugin.SqlUsername, plugin.SqlPassword);
    	        stat = conn.createStatement();
    	        stat.executeUpdate("INSERT INTO "+ MurdersList.TablePrefix +"MurdersList (world, killer, killed, weapon, armor, health, killerpot, killedpot, lasthit, time, id) VALUES ('"+ player.getKiller().getWorld().getName() +"', '"+ player.getKiller().getName() +"', '"+ event.getEntity().getName() +"', '"+ player.getKiller().getItemInHand().getTypeId() +"', '"+ helmet +","+ chestplate +","+ leggings +","+ boots +"', '"+ player.getKiller().getHealth() +"', '"+ effstypeskiller +"', '"+ effstypeskilled +"', '"+ event.getEntity().getLastDamage() +"', '"+ dateFormat.format(date) +"', null)");
   	        } catch (Exception e) {
   	        	plugin.logger.info("[ERROR] [MurdersList] Couldn't put data into MySQL database!");
	        } finally {
	            if (conn != null) { try { conn.close(); } catch (SQLException ignore) {} }
	        }
    	}
    }
    
}
