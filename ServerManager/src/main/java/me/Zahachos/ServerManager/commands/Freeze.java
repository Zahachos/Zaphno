package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Freeze implements Listener, CommandExecutor {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (ConfigManager.getInstance().getConfig().isSet("frozen." + p.getUniqueId().toString())) {
			e.setTo(e.getFrom());
			MessageManager.getInstance().custom(p, ChatColor.DARK_AQUA + "You are frozen!");
		}
	}
	
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (sender.hasPermission("sm.freeze")) {
				if (args.length == 0) {
					MessageManager.getInstance().severe(sender, "Please sepcify a player!");
					return true;
				}
				Player target = null;
				for (Player test : Bukkit.getOnlinePlayers()) {
					if (test.getName().equals(args[0])) {
						target = test;
						break;
					}
				}
				if (target != null) {
					if (!ConfigManager.getInstance().getConfig().isSet("frozen." + target.getUniqueId().toString())) {
						ConfigManager.getInstance().getConfig().set("frozen." + target.getUniqueId().toString(), true);
						ConfigManager.getInstance().saveConfig();
						MessageManager.getInstance().good(sender, target.getName() + " has been frozen!");
						MessageManager.getInstance().custom(target, ChatColor.RED + "" + ChatColor.BOLD + sender.getName() + ChatColor.RESET + ChatColor.RED + " has frozen you!");
						return true;
					} else {
						ConfigManager.getInstance().getConfig().set("frozen." + target.getUniqueId().toString(), null);
						ConfigManager.getInstance().saveConfig();
						MessageManager.getInstance().good(sender, target.getName() + " has been unfrozen!");
						MessageManager.getInstance().custom(target, ChatColor.GREEN + "" + ChatColor.BOLD + sender.getName() + ChatColor.RESET + ChatColor.GREEN + " has unfrozen you!");
						return true;
					}
				} else {
					MessageManager.getInstance().severe(sender, "Either that player doesn't exist or he isn't online!");
					return true;
				}
			}
			MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
		}
		return true;
	}
}