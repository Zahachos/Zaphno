package me.Zahachos.punish.punishments;

import me.Zahachos.punish.events.PlayerBanClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Ban implements Listener {
    
    @EventHandler
    public void onPlayerClickBan(PlayerBanClickEvent e) {
        //TODO: Check if is a tempban, then ban player
    }
}
