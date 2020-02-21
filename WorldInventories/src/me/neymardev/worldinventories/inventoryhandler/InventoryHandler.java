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
    private HashMap<String, List<String>> groups_;
    //--------------------------------------------------------------	

    public InventoryHandler(Main plugin, HashMap<String, List<String>> worldGroups) {
        this.plugin = plugin;
        world_inventories_ = new HashMap<String, HashMap<UUID, ItemStack[]>>();
        groups_ = worldGroups;
        createNestedHashMaps();
    }

    public boolean isInvChangeNeeded(String from_world, String dest_world) {

        for(String key: groups_.keySet()) {
            List<String> tempList = groups_.get(key);
            if(tempList.contains(dest_world) && tempList.contains(from_world))
                return false;
        }

        return true;
    }

    private void createNestedHashMaps() {
        for(String groupedInventories: groups_.keySet()) 
            world_inventories_.put(groupedInventories, new HashMap<UUID, ItemStack[]>());
    }

    private String findGroup(String worldName) {
        for(String groupedName: groups_.keySet()) {
            List<String> tempList = groups_.get(groupedName);
            if(tempList.contains(worldName))
                return groupedName;
        }
        Bukkit.getLogger().info("[WorldInventories] Could not find a group in the configuration for world: " + worldName);
        return null;
    }

    public void addPlayerInventory(String worldName, Player player) {
        UUID player_uuid = player.getUniqueId();
        Inventory player_Inventory = player.getInventory();
        String groupName = findGroup(worldName);
        if(groupName == null)
            return;

        HashMap<UUID, ItemStack[]> allInventories = world_inventories_.get(groupName);
        if(allInventories.containsKey(player_uuid)) {
            world_inventories_.get(groupName).replace(player_uuid, copyInventory(player_Inventory));
        }
        else 
            world_inventories_.get(groupName).put(player_uuid, copyInventory(player_Inventory));


    }

    private ItemStack[] copyInventory(Inventory inv) {
        ItemStack[] original = inv.getContents();
        ItemStack[] copy = new ItemStack[original.length];
        for(int i = 0; i < original.length; ++i)
            if(original[i] != null)
                copy[i] = new ItemStack(original[i]);
        return copy;
    }

    public void setNewInventory(Player player) {
        String worldName = player.getWorld().getName().toString();

        String groupName = findGroup(worldName);
        if(groupName == null)
            return;

        ItemStack[] playerInventory = world_inventories_.get(groupName).get(player.getUniqueId());
        if(playerInventory == null)
            return;

        player.getInventory().setContents(playerInventory);

        addPlayerInventory(worldName, player);
        //player.updateInventory();	
    }

    //--------------------------------------------------------------
    // DEBUGING FUNCTION
    public void printCurrentHashMap(String worldName) {
        String groupName = findGroup(worldName);
        if(groupName == null) 
            return;

        HashMap<UUID, ItemStack[]> player_inventory = world_inventories_.get(groupName);
        if(player_inventory.isEmpty()){
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
    //--------------------------------------------------------------
}
