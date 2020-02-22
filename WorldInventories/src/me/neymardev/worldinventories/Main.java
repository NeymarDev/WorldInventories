package me.neymardev.worldinventories;

import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.neymardev.worldinventories.confighandler.ConfigHandler;
import me.neymardev.worldinventories.inventoryhandler.InventoryHandler;
import me.neymardev.worldinventories.listeners.EventListener;

public class Main extends JavaPlugin{
    //--------------------------------------------------------------
    // class members
    private static Main plugin;
    private static InventoryHandler inventoryHandler;
    private static ConfigHandler cfgHandler;
    //--------------------------------------------------------------

    @Override
    public void onEnable() {
        plugin = this;
        cfgHandler = new ConfigHandler(plugin);
        cfgHandler.loadConfiguration();
        HashMap<String, List<String>> worldGroups = cfgHandler.readConfiguration();

        inventoryHandler = new InventoryHandler(plugin, worldGroups);
        getServer().getPluginManager().registerEvents(new EventListener(inventoryHandler), this);
        System.out.print("[WorldInventories] ready!");
        //getCommand("toastmessage").setExecutor(new ToastMessengerCommandExecutor(plugin));
    }

    @Override
    public void onDisable() {
        System.out.print("[WI] Stopping worldinventories!");
        plugin = null;
        inventoryHandler = null;
        super.onDisable();
    }

}