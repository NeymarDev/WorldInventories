package me.neymardev.worldinventories.inventoryhandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.filefilter.AndFileFilter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neymardev.worldinventories.Main;
import me.neymardev.worldinventories.confighandler.ConfigHandler;

public class InventoryHandler {
	//--------------------------------------------------------------
	// class members
	private Main plugin;
	private HashMap<String, HashMap<UUID, ItemStack[]>> world_inventories;
	private HashMap<String, List<String>> groups;
	//--------------------------------------------------------------	
	
	public InventoryHandler(Main plugin, HashMap<String, List<String>> worldGroups) {
		this.plugin = plugin;
		world_inventories = new HashMap<String, HashMap<UUID, ItemStack[]>>();
		groups = worldGroups;
		createNestedHashMaps();
	}

	public boolean isInvChangeNeeded(String from_world, String dest_world) {
		
		for(String key: groups.keySet()) {
			List<String> tempList = groups.get(key);
			if(key.equalsIgnoreCase(from_world)) {
				if(groups.get(key).contains(dest_world))
					return false;				
			}
			else if (key.equalsIgnoreCase(dest_world)) {
				if(groups.get(key).contains(from_world))
					return false;
			}
			else {
				if(tempList.contains(dest_world) && tempList.contains(from_world))
					return false;
			}
		}
		
		return true;
	}
	
	private void createNestedHashMaps() {
		for(String world: groups.keySet()) 
			world_inventories.put(world, new HashMap<UUID, ItemStack[]>());
	}
	
	private String findMapKey(String worldName) {
		if(groups.containsKey(worldName))
			return worldName;
		
		for(String key: groups.keySet()) {
			List<String> tempList = groups.get(key);
			if(tempList.contains(worldName))
				return key;
		}
		
		return null;
	}

	public void addPlayerInventory(String worldName, Player player) {
		UUID player_uuid = player.getUniqueId();
		Inventory player_Inventory = player.getInventory();
		worldName = findMapKey(worldName);
		
		HashMap<UUID, ItemStack[]> allInventories = world_inventories.get(worldName);
		if(allInventories.containsKey(player_uuid))
		{
			Bukkit.broadcastMessage("Replacing inv: " + worldName);
			world_inventories.get(worldName).replace(player_uuid, copyInventory(player_Inventory));
		}
		else 
			world_inventories.get(worldName).put(player_uuid, copyInventory(player_Inventory));
	}
	
	private ItemStack[] copyInventory(Inventory inv) {
        ItemStack[] original = inv.getContents();
        ItemStack[] copy = new ItemStack[original.length];
        for(int i = 0; i < original.length; ++i)
            if(original[i] != null)
                copy[i] = new ItemStack(original[i]);
        return copy;
    }
	
	public void printCurrentHashMap(String world_name) {
		Bukkit.broadcastMessage("PRINTING FOR WORLD " + world_name);
		HashMap<UUID, ItemStack[]> player_inventory = world_inventories.get(world_name);
		if(player_inventory.isEmpty()){
			Bukkit.broadcastMessage("Hash map empty");
			return;
		}
		for(UUID uuid: player_inventory.keySet()) {
			for(ItemStack item: player_inventory.get(uuid))
			{				
				if(item == null)
					continue;
				Bukkit.broadcastMessage("value: " + item.getType().toString());
			}
		}
	}
	
	//https://bukkit.org/threads/serialize-inventory-to-single-string-and-vice-versa.92094/
	public void setPlayerInventory(Player player) {
		String worldName = findMapKey(player.getWorld().getName());
		Bukkit.broadcastMessage("World: " + worldName + " player: " + player.getName());
		ItemStack[] playerInventory = world_inventories.get(worldName).get(player.getUniqueId());
		if(playerInventory == null)
			return;
		for(ItemStack item: playerInventory)
		{
			if(item == null)
				continue;
			Bukkit.broadcastMessage("GOT ITEM: " + item.getType().toString());
		}
		
		
		if(playerInventory != null)
		{
			Bukkit.broadcastMessage("Updating player inventory");
			Bukkit.broadcastMessage("Inv contents: " + Arrays.toString(player.getInventory().getContents()));
			player.getInventory().setContents(playerInventory);
			
			addPlayerInventory(worldName, player);
			//player.updateInventory();
		}
	}
	
	
	
}
