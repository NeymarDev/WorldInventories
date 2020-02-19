package me.neymardev.worldinventories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.jar.Attributes.Name;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.neymardev.worldinventories.events.EventListener;
import me.neymardev.worldinventories.inventoryhandler.InventoryHandler;

public class Main extends JavaPlugin{
	private static Main plugin;
	private static InventoryHandler inventoryHandler;
  
	 @Override
	 public void onEnable() {
		 System.out.print("Starting worldinventories!");
		 plugin = this;
		 inventoryHandler = new InventoryHandler(plugin);
		 
		 
		 
		 getServer().getPluginManager().registerEvents(new EventListener(inventoryHandler), this);
		 //getCommand("toastmessage").setExecutor(new ToastMessengerCommandExecutor(plugin));
	 }
 
	 @Override
	public void onDisable() {
	 System.out.print("Stopping worldinventories!");
	 plugin = null;
	 inventoryHandler = null;
	 super.onDisable();
	}
	 
	
	
}
