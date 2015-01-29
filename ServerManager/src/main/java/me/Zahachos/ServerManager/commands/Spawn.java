package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.managers.MessageManager;
import me.Zahachos.ServerManager.util.utilLocation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("spawn")) {
			if (ConfigManager.getInstance().getConfig().getConfigurationSection("spawn") == null) {
				sender.sendMessage(ChatColor.RED + "You have to set a spawn first! Use /setspawn!");
				return true;
			}
			Player p = (Player) sender;
			p.teleport(utilLocation.getInstance().getConfigLocation("spawn"));

		} else if (commandLabel.equalsIgnoreCase("setspawn")) {
			if (sender.hasPermission("sm.setspawm")) {
				if (!(sender instanceof Player)) {
					MessageManager.getInstance().severe(sender, "You need to be a player to perform this command!");
					return true;
				}
				Player p = (Player) sender;
				utilLocation.getInstance().saveConfigLocation(p.getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), p, "spawn");
				ConfigManager.getInstance().saveConfig();
				MessageManager.getInstance().good(p, "Server spawn is now set to where you stand!");
				return true;
			}
			MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
		}
		return true;
	}
}