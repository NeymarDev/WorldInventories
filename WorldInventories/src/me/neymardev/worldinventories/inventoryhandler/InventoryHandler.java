package me.neymardev.worldinventories.inventoryhandler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neymardev.worldinventories.Main;
import me.neymardev.worldinventories.experience.PlayerExperienceHolder;

public class InventoryHandler {
    //--------------------------------------------------------------
    // class members
    private HashMap<String, HashMap<UUID, ItemStack[]>> inventories_;
    private HashMap<String, HashMap<UUID, PlayerExperienceHolder>> experience_;
    private HashMap<String, List<String>> groups_;
    //--------------------------------------------------------------

    public InventoryHandler(Main plugin, HashMap<String, List<String>> worldGroups) {
        inventories_ = new HashMap<String, HashMap<UUID, ItemStack[]>>();
        experience_ = new HashMap<String, HashMap<UUID, PlayerExperienceHolder>>();
        groups_ = worldGroups;
        createNestedHashMaps();
    }

    private void createNestedHashMaps() {
        for(String groupedInventories: groups_.keySet())
        {
            inventories_.put(groupedInventories, new HashMap<UUID, ItemStack[]>());
            experience_.put(groupedInventories, new HashMap<UUID, PlayerExperienceHolder>());
        }
    }

    public boolean isInvChangeNeeded(String from_world, String dest_world) {

        for(String key: groups_.keySet()) {
            List<String> tempList = groups_.get(key);
            if(tempList.contains(dest_world) && tempList.contains(from_world))
                return false;
        }

        return true;
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

    public void storePlayerInventory(String worldName, Player player) {
        UUID player_uuid = player.getUniqueId();
        Inventory player_Inventory = player.getInventory();
        String groupName = findGroup(worldName);
        if(groupName == null)
            return;

        HashMap<UUID, ItemStack[]> allInventories = inventories_.get(groupName);
        if(allInventories.containsKey(player_uuid)) {
            inventories_.get(groupName).replace(player_uuid, copyInventory(player_Inventory));
            experience_.get(groupName).replace(player_uuid, new PlayerExperienceHolder(player));
        }
        else {
            inventories_.get(groupName).put(player_uuid, copyInventory(player_Inventory));
            experience_.get(groupName).put(player_uuid, new PlayerExperienceHolder(player));
        }
    }

    private ItemStack[] copyInventory(Inventory inv) {
        ItemStack[] original = inv.getContents();
        ItemStack[] copy = new ItemStack[original.length];
        for(int i = 0; i < original.length; ++i)
            if(original[i] != null)
                copy[i] = new ItemStack(original[i]);
        return copy;
    }

    public void loadPlayerInventory(Player player) {
        String worldName = player.getWorld().getName().toString();

        String groupName = findGroup(worldName);
        if(groupName == null)
            return;

        UUID player_uuid = player.getUniqueId();
        ItemStack[] playerInventory = inventories_.get(groupName).get(player_uuid);
        if(playerInventory == null)
            return;

        PlayerExperienceHolder experienceHolder = experience_.get(groupName).get(player_uuid);
        if(experienceHolder == null)
            experienceHolder = new PlayerExperienceHolder();

        player.getInventory().setContents(playerInventory);
        player.setExp(experienceHolder.getHolderExp());
        player.setLevel(experienceHolder.getHolderLevel());

        storePlayerInventory(worldName, player);
        //player.updateInventory();
    }
}
