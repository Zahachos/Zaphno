package me.Zahachos.ServerManager.util;

import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.util.utilTime.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Cooldown {

	static ConfigManager settings = ConfigManager.getInstance();

	public static HashMap<UUID, utilCooldown> cooldownPlayers = new HashMap<UUID, utilCooldown>();

	public static void add(UUID player, String ability, long seconds, long systime) {
		if(!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new utilCooldown(player));
		if(isCooling(player, ability)) return;
		utilCooldown.cooldownMap.put(ability, new utilCooldown(player, seconds * 1000, System.currentTimeMillis()));
	}

	public static boolean isCooling(UUID player, String ability) {
		if(!cooldownPlayers.containsKey(player)) return false;
		if(!utilCooldown.cooldownMap.containsKey(ability)) return false;
		return true;
	}

	public static double getRemaining(UUID player, String ability) {
		if(!cooldownPlayers.containsKey(player)) return 0.0;
		if(!utilCooldown.cooldownMap.containsKey(ability)) return 0.0;
		return utilTime.convert((utilCooldown.cooldownMap.get(ability).seconds + utilCooldown.cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}

	public static void removeCooldown(UUID key, String ability) {
		if(!cooldownPlayers.containsKey(key)) {
			return;
		}
		if(!utilCooldown.cooldownMap.containsKey(ability)) {
			return;
		}
		utilCooldown.cooldownMap.remove(ability);
	}
	
    static utilLocation loc = utilLocation.getInstance();
    
    public static ArrayList<String> isAfk = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public static void handleCooldowns() {
		if(cooldownPlayers.isEmpty()) {
			return;
		}
		for(Iterator<UUID> it = cooldownPlayers.keySet().iterator(); it.hasNext();) {
			UUID key = it.next();
			cooldownPlayers.get(key);
			for(Iterator<String> iter = utilCooldown.cooldownMap.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				if(getRemaining(key, name) <= 0.0) {
					if (isCooling(key, "TempBan")) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(key);
						p.setBanned(false);
						removeCooldown(key, name);
					}
					if (isCooling(key, "Mute")) {
						removeCooldown(key, name);
					}
					if (isCooling(key, "afk")) {
						if (settings.getConfig().isSet("afk_area")) {
							removeCooldown(key, name);
							Player p = Bukkit.getServer().getPlayer(key);
							p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been afk for too long!");
							return;
						}
					}
				}
				if(getTime(key, name) <= 180000.0 && getTime(key, name) >= 179950.0) {
					if (isCooling(key, "afk")) {
						if (settings.getConfig().isSet("afk_area")) {
							Player p = Bukkit.getServer().getPlayer(key);
							p.sendMessage(ChatColor.YELLOW + "You are now afk!");
							loc.savePlayerLocation(p.getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), p, "afk_at");
							p.teleport(loc.getConfigLocation("afk_area"));
							isAfk.add(p.getName());
							return;
						}
					}
				}
			}
		}
	}
	
	public static long getTime(UUID player, String ability) {
		return (utilCooldown.cooldownMap.get(ability).seconds + utilCooldown.cooldownMap.get(ability).systime) - System.currentTimeMillis();
	}
}