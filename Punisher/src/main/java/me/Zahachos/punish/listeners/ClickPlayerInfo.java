package me.Zahachos.punish.listeners;

import me.Zahachos.punish.Main;
import me.Zahachos.punish.events.PlayerInfoClickEvent;
import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class ClickPlayerInfo implements Listener {

	@EventHandler
	public void PlayerInfoEvent(PlayerInfoClickEvent e) {

		File player = new File(Main.plugin.getDataFolder() + "/players/" + e.getPlayer().getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);

		if (!(config.contains("banned") && config.contains("mute") && config.contains("kick"))) {
			MessageManager.getInstance().info(e.getWhoClicked(), "Player " + e.getPlayer().getName() + ChatColor.YELLOW + " doesn't have any previous infractions!");
		}
		e.getWhoClicked().closeInventory();
	}
	
}
