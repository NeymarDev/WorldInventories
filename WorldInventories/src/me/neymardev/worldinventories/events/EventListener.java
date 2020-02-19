package me.neymardev.worldinventories.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.neymardev.worldinventories.Main;

public class EventListener implements Listener{
	private Main plugin;
	
	
	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		Bukkit.broadcastMessage("Player " + player.getName() + " moved from " + 
				event.getFrom().getName() + " to " + player.getWorld().getName());
		
		plugin.addPlayerInventory(event.getFrom().getName(), event.getPlayer());
		plugin.printCurrentHashMap(event.getFrom().getName());
		plugin.printCurrentHashMap(player.getWorld().getName());
		
		player.getInventory().clear();
		Bukkit.broadcastMessage("After clearing");
		plugin.printCurrentHashMap(event.getFrom().getName());
		plugin.setPlayerInventory(player);
		
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
}
