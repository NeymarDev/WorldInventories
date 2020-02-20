package me.neymardev.worldinventories.inventoryhandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.filefilter.AndFileFilter;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neymardev.worldinventories.Main;
import me.neymardev.worldinventories.confighandler.ConfigHandler;

public class InventoryHandler {
	//--------------------------------------------------------------
	// class members
	private Main plugin;
	private HashMap<String, HashMap<UUID, ItemStack[]>> world_inventories_;
	private HashMap<String, List<String>> world_groups_;
	//--------------------------------------------------------------	
	
	public InventoryHandler(Main plugin, HashMap<String, List<String>> worldGroups) {
		this.plugin = plugin;
		world_inventories_ = new HashMap<String, HashMap<UUID, ItemStack[]>>();
		world_groups_ = worldGroups;
		createNestedHashMaps();
	}

	public boolean isInvChangeNeeded(String from_world, String dest_world) {
		
		for(String key: world_groups_.keySet()) {
			List<String> tempList = world_groups_.get(key);
			if(tempList.contains(dest_world) && tempList.contains(from_world))
				return false;
		}
		
		return true;
	}
	
	private void createNestedHashMaps() {
		for(String groupedInventories: world_groups_.keySet()) 
			world_inventories_.put(groupedInventories, new HashMap<UUID, ItemStack[]>());
	}
	
	private String findGroup(String worldName) {
		Bukkit.broadcastMessage("Looking for: " + worldName + " in groups: " + world_groups_ + "\n\n");
		for(String groupedName: world_groups_.keySet()) {
			List<String> tempList = world_groups_.get(groupedName);
			if(tempList.contains(worldName))
				return groupedName;
		}
		return null;
	}

	public void addPlayerInventory(String worldName, Player player) {
		UUID player_uuid = player.getUniqueId();
		Inventory player_Inventory = player.getInventory();
		String groupName = findGroup(worldName);
		if(groupName == null) {
			Bukkit.broadcastMessage("[WI]: Group not found in config!");
			return;
		}
		
		Bukkit.broadcastMessage("Received worldname: " + worldName + " group: " + groupName);
		HashMap<UUID, ItemStack[]> allInventories = world_inventories_.get(groupName);
		if(allInventories.containsKey(player_uuid)) {
			world_inventories_.get(groupName).replace(player_uuid, copyInventory(player_Inventory));
		}
		else 
			world_inventories_.get(groupName).put(player_uuid, copyInventory(player_Inventory));
	
		player_Inventory.clear();
	}
	
	private ItemStack[] copyInventory(Inventory inv) {
        ItemStack[] original = inv.getContents();
        ItemStack[] copy = new ItemStack[original.length];
        for(int i = 0; i < original.length; ++i)
            if(original[i] != null)
                copy[i] = new ItemStack(original[i]);
        return copy;
    }
	
	public void printCurrentHashMap(String worldName) {
		Bukkit.broadcastMessage("PRINTING FOR WORLD " + worldName);
		String groupName = findGroup(worldName);
		if(groupName == null) {
			Bukkit.broadcastMessage("[WI]: Group not found in config!");
			return;
		}
		HashMap<UUID, ItemStack[]> player_inventory = world_inventories_.get(groupName);
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
		String worldName = player.getWorld().getName().toString();
		
		String groupName = findGroup(worldName);
		if(groupName == null)
		{
			Bukkit.broadcastMessage("[WI]: Group not found in config!");
			return;
		}
		Bukkit.broadcastMessage("World: " + worldName + " player: " + player.getName() + " found group: "+ groupName);
		
		ItemStack[] playerInventory = world_inventories_.get(groupName).get(player.getUniqueId());
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
			Bukkit.broadcastMessage("Inv contents: " + Arrays.toString(playerInventory));
			player.getInventory().setContents(playerInventory);
			
			addPlayerInventory(worldName, player);
			//player.updateInventory();
		}
	}
	
	
	
}
