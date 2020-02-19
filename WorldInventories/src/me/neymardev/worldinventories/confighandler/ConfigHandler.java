package me.neymardev.worldinventories.confighandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.neymardev.worldinventories.Main;

public class ConfigHandler {
	
	//--------------------------------------------------------------
	// class members
	private static Main plugin;
	
	//--------------------------------------------------------------
	
	public ConfigHandler(Main pl) {
		this.plugin = pl;
	}
	
	public void loadConfiguration() {
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public HashMap<String, List<String>> readConfiguration() {
		HashMap<String, List<String>> worldGroups = new HashMap<String, List<String>>();
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("inv");
		for(String key: section.getKeys(false)) {
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) plugin.getConfig().getList("inv." + key);
			worldGroups.put(key, values);
		}
	
		return worldGroups;
	}
}
