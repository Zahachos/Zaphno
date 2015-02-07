package me.Tecno_Wizard.CakeAttack.events;

import org.bukkit.event.HandlerList;

/**
 * Created by Ethan on 2/7/2015.
 */
public class PlayerJoinRequest {
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
