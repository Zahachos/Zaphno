package me.Zahachos.punish;

import me.Zahachos.punish.events.PlayerInfoClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EventCaller implements Listener {

	private Player p;

	@EventHandler
	public void OnInventoryClick(InventoryClickEvent e) {
		if(!e.getInventory().equals(Punish.inv)) { return; }
		e.setCancelled(true);
		if (e.getClick().isLeftClick()) {
			p = (Player) e.getWhoClicked();
			try {
				if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					Bukkit.broadcastMessage(e.getSlotType().name());
					Bukkit.getServer().getPluginManager().callEvent(new PlayerInfoClickEvent(Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getLore().get(0)), p, Punish.inv));
				}
			} catch(Exception exe) {
				exe.printStackTrace();
			}
		}
	}
}