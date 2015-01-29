package me.Zahachos.ServerManager.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

	ConfigManager() { }

	static ConfigManager instance = new ConfigManager();

	public static ConfigManager getInstance() {
		return instance;
	}

	Plugin p;

	FileConfiguration config;
	File cfile;

	FileConfiguration data;
	File dfile;

	public void setup(Plugin p) {
		config = p.getConfig();
		config.options().copyDefaults(true);
		cfile = new File(p.getDataFolder(), "config.yml");
		saveConfig();

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dfile = new File(p.getDataFolder(), "data.yml");

		if (!dfile.exists()) {
			try {
				dfile.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
			}
		}
		data = YamlConfiguration.loadConfiguration(dfile);
	}

	public FileConfiguration getData() {
		return data;
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dfile);
	}
	public void saveData() {
		try {
			data.save(dfile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(cfile);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}
	
	

}

