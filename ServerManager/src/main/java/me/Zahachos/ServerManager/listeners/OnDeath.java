package me.Zahachos.ServerManager.listeners;

import me.Zahachos.ServerManager.apis.fanciful.mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeath implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		String msg = e.getDeathMessage();
		e.setDeathMessage(null);

		for (Player p : Bukkit.getOnlinePlayers()) {

			new FancyMessage(ChatColor.RED + "" + ChatColor.BOLD + "Player " + e.getEntity().getName() + " died!")
			.tooltip(ChatColor.GOLD + msg + "!")
			.send(p);
		}
	}
}