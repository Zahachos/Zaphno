package me.Zahachos.punish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInfoClickEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	Player clicker;

	public PlayerInfoClickEvent(Player clicker) {
		this.clicker = clicker;
	}

	public Player getWhoClicked() {
		return clicker;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
