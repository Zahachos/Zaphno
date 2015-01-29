package me.Zahachos.punish.cooldowns;

import java.util.HashMap;
import java.util.UUID;
 
public class utilCooldown {
 
	public UUID player = null;
    public long seconds;
    public long systime;
 
    public static HashMap<String, utilCooldown> cooldownMap = new HashMap<String, utilCooldown>();
 
    public utilCooldown(UUID player2, long seconds, long systime) {
        this.player = player2;
        this.seconds = seconds;
        this.systime = systime;
    }
 
    public utilCooldown(UUID player) {
        this.player = player;
    }
}
