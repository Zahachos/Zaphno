package me.Zahachos.punish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class OpenNewInventoryEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    Inventory inv;
    Player clicker;

    public OpenNewInventoryEvent(Player clicker, Inventory inv) {
        this.inv = inv;
        this.clicker = clicker;
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
