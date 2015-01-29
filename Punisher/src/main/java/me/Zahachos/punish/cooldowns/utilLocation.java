package me.Zahachos.punish.cooldowns;

import me.Zahachos.punish.Main;
import me.Zahachos.punish.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class utilLocation {
	
	static utilLocation instance = new utilLocation();

	public static utilLocation getInstance() {
		return instance;
	}
	
	static ConfigManager settings = ConfigManager.getInstance();
	
	public void savePlayerLocation(String world, double x, double y, double z, float yaw, float pitch, Player p, String section) {
		File player = new File(Main.plugin.getDataFolder() + "/players/" + p.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);
		config.set(section + ".world", p.getLocation().getWorld().getName());
		config.set(section + ".x", p.getLocation().getX());
		config.set(section + ".y", p.getLocation().getY());
		config.set(section + ".z", p.getLocation().getZ());
		config.set(section + ".yaw", p.getLocation().getYaw());
		config.set(section + ".pitch", p.getLocation().getPitch());
		try {
			config.save(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfigLocation(String world, double x, double y, double z, float yaw, float pitch, Player p, String section) {
		settings.getConfig().set(section + ".world", p.getLocation().getWorld().getName());
		settings.getConfig().set(section + ".x", p.getLocation().getX());
		settings.getConfig().set(section + ".y", p.getLocation().getY());
		settings.getConfig().set(section + ".z", p.getLocation().getZ());
		settings.getConfig().set(section + ".yaw", p.getLocation().getYaw());
		settings.getConfig().set(section + ".pitch", p.getLocation().getPitch());
		settings.saveConfig();
	}
	
	public Location getConfigLocation(String section) {
		Location location = new Location(Bukkit.getWorld(settings.getConfig().getString(section + ".world")), settings.getConfig().getDouble(section + ".x"), settings.getConfig().getDouble(section + ".y"), settings.getConfig().getDouble(section + ".z"));
		location.setPitch((float) settings.getConfig().getDouble(section + ".pitch"));
		location.setYaw((float) settings.getConfig().getDouble(section + ".yaw"));
		return location;
	}
	
	public Location getPlayerLocation(Player p, String section) {
		File player = new File(Main.plugin.getDataFolder() + "/players/" + p.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);
		Location location = new Location(Bukkit.getWorld(config.getString(section + ".world")), config.getDouble(section + ".x"), config.getDouble(section + ".y"), config.getDouble(section + ".z"));
		location.setPitch((float) config.getDouble(section + ".pitch"));
		location.setYaw((float) config.getDouble(section + ".yaw"));
		return location;
	}
	
	public void setPlayerLocationNull(Player p, String section) {
		File player = new File(Main.plugin.getDataFolder() + "/players/" + p.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);
		config.set(section, null);
		try {
			config.save(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
