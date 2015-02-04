package me.Tecno_Wizard.CakeAttack.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class Listeners implements Listener{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        //TODO: Teleport to main hub locations
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    // will create custom events
    @EventHandler
    public void onPlayerJoinField(){

    }
}
