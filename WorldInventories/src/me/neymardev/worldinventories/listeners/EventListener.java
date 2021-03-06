package me.neymardev.worldinventories.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.neymardev.worldinventories.inventoryhandler.InventoryHandler;

public class EventListener implements Listener{
    //--------------------------------------------------------------
    // class members
    private InventoryHandler inventoryHandler;
    //--------------------------------------------------------------


    public EventListener(InventoryHandler handler) {
        this.inventoryHandler = handler;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        Bukkit.broadcastMessage("Player " + player.getName() + " moved from " +
                event.getFrom().getName() + " to " + player.getWorld().getName());

        if(!inventoryHandler.isInvChangeNeeded(event.getFrom().getName(), player.getWorld().getName()))
            return;

        Bukkit.broadcastMessage("Inv change needed!!");

        inventoryHandler.storePlayerInventory(event.getFrom().getName(), event.getPlayer());

        event.getPlayer().getInventory().clear();
        event.getPlayer().setExp(0);
        event.getPlayer().setLevel(0);

        inventoryHandler.loadPlayerInventory(player);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }
}
