package me.Zahachos.ServerManager.listeners;

import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.util.utilLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnReconnect implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (p.getWorld().getName().equals("Faction")) {
			utilLocation.getInstance().savePlayerLocation(p.getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), p, "faction.location");
			return;
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (ConfigManager.getInstance().getConfig().isSet("spawn")) {
			p.teleport(utilLocation.getInstance().getConfigLocation("spawn"));
		}
	}

	@EventHandler
	public void onWorldChange(PlayerTeleportEvent e) {
		Player p = e.getPlayer();

		String w = e.getFrom().getWorld().getName();
		String wt = e.getPlayer().getWorld().getName();

		if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) {
			
			if (w.equals("Faction")) {
				utilLocation.getInstance().savePlayerLocation(w, e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getFrom().getYaw(), e.getFrom().getPitch(), p, "faction.location");
				return;
			}
			
			switch(wt) {
			
			case "Faction": Bukkit.broadcastMessage("test");
			p.teleport(utilLocation.getInstance().getPlayerLocation(p, "faction.location"));
			break;
			
			case "Hub": 
			}
			if (wt.equals("Faction")) {
				Bukkit.broadcastMessage("test");
				p.teleport(utilLocation.getInstance().getPlayerLocation(p, "faction.location"));
				return;
			}
		}
	}
}