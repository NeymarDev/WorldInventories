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

public class Main extends JavaPlugin{
	private static Main plugin;
	HashMap<String, HashMap<UUID, ItemStack[]>> world_inventories;
  
	 @Override
	 public void onEnable() {
		 System.out.print("Starting worldinventories!");
		 plugin = this;
		 world_inventories = new HashMap<String, HashMap<UUID, ItemStack[]>>();
		 createNestedHashMaps();
		 
		 
		 getServer().getPluginManager().registerEvents(new EventListener(plugin), this);
		 //getCommand("toastmessage").setExecutor(new ToastMessengerCommandExecutor(plugin));
	 }
 
	 @Override
	public void onDisable() {
	 System.out.print("Stopping worldinventories!");
	 plugin = null;
	 world_inventories = null;
	 super.onDisable();
	}
	 
	
	public void createNestedHashMaps() {
		String[] worldName = getWorldNames();
		
		for(String world: worldName) 
			world_inventories.put(world, new HashMap<UUID, ItemStack[]>());
		worldName = null;
	}
	
	public String[] getWorldNames() {
		String[] worldNames = new String[Bukkit.getServer().getWorlds().size()];
		int count = 0;
		for(World w : Bukkit.getServer().getWorlds()) {
			worldNames[count] = w.getName(); 
			count++;
		}
		for(String s : worldNames){
		System.out.println("World Names = " + s);
		}
		return worldNames;
	}

	public void addPlayerInventory(String world_name, Player player) {
		player.sendMessage("\n\n\nSaving inventory to hashmap");
		UUID player_uuid = player.getUniqueId();
		Inventory player_Inventory = player.getInventory();
		
		HashMap<UUID, ItemStack[]> allInventories = world_inventories.get(world_name);
		if(allInventories.containsKey(player_uuid))
		{
			Bukkit.broadcastMessage("Replacing inv: " + world_name);
			world_inventories.get(world_name).replace(player_uuid, copyInventory(player_Inventory));
		}
		else 
			world_inventories.get(world_name).put(player_uuid, copyInventory(player_Inventory));
	}
	
	private ItemStack[] copyInventory(Inventory inv)
    {
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
		String worldName = player.getWorld().getName();
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
