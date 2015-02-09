package me.Tecno_Wizard.CakeAttack.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

/**
 * Created by Ethan on 2/7/2015.
 */
public class PlayerJoinRequestEvent extends Event{
    Player player;
    int lobby;

    public PlayerJoinRequestEvent(Player player, int lobby) {
        this.player = player;
        this.lobby = lobby;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLobbyNumber() {
        return lobby;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
