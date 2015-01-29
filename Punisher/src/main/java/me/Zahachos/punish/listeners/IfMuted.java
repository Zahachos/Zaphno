package me.Zahachos.punish.listeners;

import me.Zahachos.punish.Main;
import me.Zahachos.punish.cooldowns.Cooldown;
import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IfMuted implements Listener, CommandExecutor {

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		return true;
	}

	public boolean isMuted(Player play) {
		File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);

		if (config.getBoolean("muted.perm.state")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMuted(OfflinePlayer play) {
		File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(player);

		if (config.getBoolean("muted.perm.state")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTempMuted(Player play) {
		if (Cooldown.isCooling(play.getUniqueId(), "Mute")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTempMuted(OfflinePlayer play) {
		if (Cooldown.isCooling(play.getUniqueId(), "Mute")) {
			return true;
		} else {
			return false;
		}
	}
	

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player play = e.getPlayer();
		if (!play.hasPermission("sm.mute.canChat")) {
			if (Cooldown.isCooling(play.getUniqueId(), "Mute")) {

				e.setCancelled(true);

				File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
				FileConfiguration config = YamlConfiguration.loadConfiguration(player);
				List<String> list = config.getStringList("muted.temp.reason");
				String reason = list.get(list.size() - 1);

				if (Cooldown.getTime(play.getUniqueId(), "Mute") < 60000L) {
					MessageManager.getInstance().custom(play, ChatColor.YELLOW + "You are still muted for " + Cooldown.getRemaining(play.getUniqueId(), "Mute") + " seconds." + ChatColor.RED + " Reason: " + reason);
					return;
				}
				if (Cooldown.getTime(play.getUniqueId(), "Mute") < 3600000L) {
					MessageManager.getInstance().custom(play, ChatColor.YELLOW + "You are still muted for " + Cooldown.getRemaining(play.getUniqueId(), "Mute") + " minutes." + ChatColor.RED + " Reason: " + reason);
					return;
				}
				if (Cooldown.getTime(play.getUniqueId(), "Mute") < 86400000L) {
					MessageManager.getInstance().custom(play, ChatColor.YELLOW + "You are still muted for " + Cooldown.getRemaining(play.getUniqueId(), "Mute") + " hours." + ChatColor.RED + " Reason: " + reason);
					return;
				}
				MessageManager.getInstance().custom(play, ChatColor.YELLOW + "You are still muted for " + Cooldown.getRemaining(play.getUniqueId(), "Mute") + " days." + ChatColor.RED + " Reason: " + reason);
				return;
			}
			if (isMuted(play)) {

				e.setCancelled(true);

				File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
				FileConfiguration config = YamlConfiguration.loadConfiguration(player);
				List<String> list = config.getStringList("muted.perm.reason");
				String reason = list.get(list.size() - 1);

				MessageManager.getInstance().custom(play, ChatColor.YELLOW + "You are permanently muted!" + ChatColor.RED + " Reason: " + reason);
			}
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tempmute")) {
			if (sender.hasPermission("sm.mute.tempmute")) {
				if (args.length == 0) {
					MessageManager.getInstance().severe(sender, "Please specify who you want to temporarily mute, the duration and the reason!");
					return true;
				}
				if (args.length == 1) {
					MessageManager.getInstance().severe(sender, "Please specify the duration and the reason of the temporary mute!");
					return true;
				}
				if (args.length == 2) {
					MessageManager.getInstance().severe(sender, "Please specify the reason of the temporary mute!");
					return true;
				} else {
					OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
					if (target == null) {
						MessageManager.getInstance().severe(sender, "Player doesn't exist!");
						return true;
					} else if (!isInteger(args[1])) {
						MessageManager.getInstance().severe(sender, "The duration must be a number!");
						return true;
					} else {

						int duration = Integer.parseInt(args[1]);

						StringBuilder string = new StringBuilder();
						for (int r = 2; r < args.length; r++) {
							string.append(args[r] + " ");
						}
						String rs = string.toString().trim();

						File puuidFile = new File(Main.plugin.getDataFolder() + "/players/" + target.getUniqueId() + ".yml");
						FileConfiguration playerFile = YamlConfiguration.loadConfiguration(puuidFile);

						List<String> reason = new ArrayList<String>();

						if (playerFile.isSet("muted.temp.reason")) {
							reason = playerFile.getStringList("muted.temp.reason");
						}
						reason.add(rs);
						playerFile.set("muted.temp.reason", reason);
						try {
							playerFile.save(puuidFile);
						} catch (IOException e) {
							e.printStackTrace();
						}

						Cooldown.add(target.getUniqueId(), "Mute", duration, System.currentTimeMillis());

						if (Cooldown.getTime(target.getUniqueId(), "Mute") < 60000L) {
							MessageManager.getInstance().custom(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(target.getUniqueId(), "Mute") + " seconds." + ChatColor.RED + " Reason: " + rs);
						} else if (Cooldown.getTime(target.getUniqueId(), "Mute") < 3600000L) {
							MessageManager.getInstance().custom(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(target.getUniqueId(), "Mute") + " minutes." + ChatColor.RED + " Reason: " + rs);
						} else if (Cooldown.getTime(target.getUniqueId(), "Mute") < 86400000L) {
							MessageManager.getInstance().custom(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(target.getUniqueId(), "Mute") + " hours." + ChatColor.RED + " Reason: " + rs);
						} else {
							MessageManager.getInstance().custom(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(target.getUniqueId(), "Mute") + " days." + ChatColor.RED + " Reason: " + rs);
						}
						if (target.isOnline()) {
							Player p = Bukkit.getServer().getPlayer(args[0]);

							if (Cooldown.getTime(p.getUniqueId(), "Mute") < 60000L) {
								MessageManager.getInstance().custom(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(p.getUniqueId(), "Mute") + " seconds." + ChatColor.RED + " Reason: " + rs);
								return true;
							}
							if (Cooldown.getTime(p.getUniqueId(), "Mute") < 3600000L) {
								MessageManager.getInstance().custom(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(p.getUniqueId(), "Mute") + " minutes." + ChatColor.RED + " Reason: " + rs);
								return true;
							}
							if (Cooldown.getTime(p.getUniqueId(), "Mute") < 86400000L) {
								MessageManager.getInstance().custom(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(p.getUniqueId(), "Mute") + " hours." + ChatColor.RED + " Reason: " + rs);
								return true;
							}
							MessageManager.getInstance().custom(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily muted for " + Cooldown.getRemaining(p.getUniqueId(), "Mute") + " days." + ChatColor.RED + " Reason: " + rs);
							return true;
						}
						Cooldown.add(target.getUniqueId(), "Mute", duration, System.currentTimeMillis());
						return true;
					}
				}
			}
			MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			if (sender.hasPermission("sm.mute.unmute")) {
				if (args.length == 0) {
					MessageManager.getInstance().severe(sender, "Please specify who you want to unmute!");
					return true;
				}
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (target == null) {
					MessageManager.getInstance().severe(sender, "Player doesn't exist!");
					return true;
				}
				if (!isMuted(target) && !isTempMuted(target)) {
					MessageManager.getInstance().severe(sender, "That player isn't muted!");
					return true;
				}
				if (Cooldown.cooldownPlayers.containsKey(target.getUniqueId())) {
					Cooldown.removeCooldown(target.getUniqueId(), "Mute");
					MessageManager.getInstance().good(sender, target.getName() + " is no longer temporarily muted!");
					return true;
				}

				File puuidFile = new File(Main.plugin.getDataFolder() + "/players/" + target.getUniqueId() + ".yml");
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(puuidFile);
				
				playerFile.set("muted.perm.state", false);
				try {
					playerFile.save(puuidFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				MessageManager.getInstance().good(sender, target.getName() + " is no longer permanently muted!");
				return true;
			}
			MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (sender.hasPermission("sm.mute.mute")) {
				if (args.length == 0) {
					MessageManager.getInstance().severe(sender, "Please specify who you want to permanently mute and the reason!");
					return true;
				}
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (target == null) {
					MessageManager.getInstance().severe(sender, "Player doesn't exist!");
					return true;
				}
				if (args.length == 1) {
					MessageManager.getInstance().severe(sender, "Please specify a reason for permanently muting that player!");
					return true;
				}

				StringBuilder string = new StringBuilder();
				for (int r = 1; r < args.length; r++) {
					string.append(args[r] + " ");
				}
				String rs = string.toString().trim();

				File puuidFile = new File(Main.plugin.getDataFolder() + "/players/" + target.getUniqueId() + ".yml");
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(puuidFile);

				playerFile.set("muted.perm.state", true);

				List<String> reason = new ArrayList<String>();

				if (playerFile.isSet("muted.perm.reason")) {
					reason = playerFile.getStringList("muted.perm.reason");
				}
				reason.add(rs);
				playerFile.set("muted.perm.reason", reason);
				try {
					playerFile.save(puuidFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

				MessageManager.getInstance().custom(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + target.getName() + " has been permanently muted! " + ChatColor.RED + "Reason: " + rs);

				if (target.isOnline()) {
					Player p = Bukkit.getServer().getPlayer(args[0]);
					MessageManager.getInstance().custom(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been permanently muted!" + ChatColor.RED + " Reason: " + rs);
					return true;
				}
				return true;
			}
			MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
			return true;
		}
		return true;
	}
}
