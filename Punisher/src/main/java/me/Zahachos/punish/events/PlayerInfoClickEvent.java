package me.Zahachos.punish.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class PlayerInfoClickEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	OfflinePlayer p;
	Inventory inv;
	Player clicker;

	public PlayerInfoClickEvent(OfflinePlayer playername, Player clicker, Inventory inv) {
		this.p = playername;
		this.inv = inv;
		this.clicker = clicker;
	}

	public OfflinePlayer getPlayer() {
		return p;
	}

	public Player getWhoClicked() {
		return clicker;
	}

	public Inventory getInventory() {
		return inv;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
