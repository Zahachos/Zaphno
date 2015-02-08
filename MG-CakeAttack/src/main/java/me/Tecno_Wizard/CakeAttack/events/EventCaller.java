package me.Tecno_Wizard.CakeAttack.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Ethan on 2/7/2015.
 */
public class EventCaller implements Listener{
    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                String[] stripped = ChatColor.stripColor(sign.getLine(1)).split(" ");
                if (stripped[0].equalsIgnoreCase("CakeAttack")) {
                    int lobby;
                    try {
                        lobby = Integer.parseInt(stripped[1]);
                    } catch(Error error){return;}
                    PlayerJoinRequestEvent event = new PlayerJoinRequestEvent(e.getPlayer(), lobby);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        }
    }
}
