package me.Zahachos.ServerManager.util;

import me.Zahachos.ServerManager.managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class utilReloadSave {

	public static void saveBans() {
		if (!Cooldown.cooldownPlayers.isEmpty()) {
			try {
				List<String> bans = new ArrayList<String>(); 
				for (UUID play : Cooldown.cooldownPlayers.keySet()) {
					ConfigManager.getInstance().getConfig().set("bans." + play, Cooldown.getTime(play, "TempBan"));
					bans.add(play.toString());
				}
				ConfigManager.getInstance().getConfig().set("banlist", bans);
				ConfigManager.getInstance().saveConfig();
			} catch (NullPointerException e) {
				return;
			}
		}
	}
	public static void loadBans() {
		FileConfiguration config = ConfigManager.getInstance().getConfig();
		List<String> bans = config.getStringList("banlist");
		for (String uuid : bans) {
			Cooldown.add(UUID.fromString(uuid), "TempBan", config.getLong("bans." + uuid) / 1000, System.currentTimeMillis());
		}
		config.set("bans", null);
		config.set("banlist", null);
		ConfigManager.getInstance().saveConfig();
	}

	public static void saveMutes() {
		if (!Cooldown.cooldownPlayers.isEmpty()) {
			try {
			List<String> mutes = new ArrayList<String>(); 
			for (UUID play : Cooldown.cooldownPlayers.keySet()) {
				ConfigManager.getInstance().getConfig().set("mutes." + play, Cooldown.getTime(play, "Mute"));
				mutes.add(play.toString());
			}
			ConfigManager.getInstance().getConfig().set("mutelist", mutes);
			ConfigManager.getInstance().saveConfig();
			} catch (NullPointerException e) {
				return;
			}
		}
	}
	public static void loadMutes() {
		FileConfiguration config = ConfigManager.getInstance().getConfig();
		List<String> mutes = config.getStringList("mutelist");
		for (String uuid : mutes) {
			Cooldown.add(UUID.fromString(uuid), "Mute", config.getLong("mutes." + uuid) / 1000, System.currentTimeMillis());
		}
		config.set("mutes", null);
		config.set("mutelist", null);
		ConfigManager.getInstance().saveConfig();
	}
}