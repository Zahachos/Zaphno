package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealFeed implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("heal")) {

			if (sender.hasPermission("sm.health.heal")) {

				if (args.length == 0) {

					if (!(sender instanceof Player)) {

						MessageManager.getInstance().severe(sender, "The console can't heal it's self, specify a player!");
						return true;
					}

					Player p = (Player) sender;
					p.setHealth(p.getMaxHealth());
					MessageManager.getInstance().good(p, "You have been healed!");
					return true;
				}

				if (args.length == 1) {
					Player target = null;
					for (Player test : Bukkit.getOnlinePlayers()) {
						if (test.getName().equals(args[0])) {
							target = test;
							break;
						}
					}

					if (target == null) {
						MessageManager.getInstance().severe(sender, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					target.setHealth(target.getMaxHealth());
					MessageManager.getInstance().good(target, "You have been healed by " + sender.getName() + "!");
					MessageManager.getInstance().good(sender, "Player " + target.getName() + " has been healed!");
					return true;
				}

				MessageManager.getInstance().severe(sender, "Too many arguments!");
				return true;
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
		}

		if (cmd.getName().equalsIgnoreCase("feed")) {

			if (sender.hasPermission("sm.health.feed")) {

				if (args.length == 0) {

					if (!(sender instanceof Player)) {

						MessageManager.getInstance().severe(sender, "The console can't feed it's self, specify a player!");
						return true;
					}

					Player p = (Player) sender;
					p.setFoodLevel(20);
					MessageManager.getInstance().good(p, "You have fed healed!");
					return true;
				}

				if (args.length == 1) {
					Player target = null;
					for (Player test : Bukkit.getOnlinePlayers()) {
						if (test.getName().equals(args[0])) {
							target = test;
							break;
						}
					}

					if (target == null) {
						MessageManager.getInstance().severe(sender, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					target.setFoodLevel(20);
					MessageManager.getInstance().good(target, "You have been fed by " + sender.getName() + "!");
					MessageManager.getInstance().good(sender, "Player " + target.getName() + " has been fed!");
					return true;
				}

				if (args.length > 2) {
					MessageManager.getInstance().severe(sender, "Too many arguments!");
					return true;
				}
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
		}

		if (cmd.getName().equalsIgnoreCase("regenerate")) {

			if (sender.hasPermission("sm.health.regen")) {

				if (args.length == 0) {

					if (!(sender instanceof Player)) {

						MessageManager.getInstance().severe(sender, "The console can't regenerate it's self, specify a player!");
						return true;
					}

					Player p = (Player) sender;
					p.setHealth(p.getMaxHealth());
					p.setFoodLevel(20);
					MessageManager.getInstance().good(p, "You have been regenerated!");
					return true;
				}

				if (args.length == 1) {
					Player target = null;
					for (Player test : Bukkit.getOnlinePlayers()) {
						if (test.getName().equals(args[0])) {
							target = test;
							break;
						}
					}

					if (target == null) {
						MessageManager.getInstance().severe(sender, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					target.setHealth(target.getMaxHealth());
					target.setFoodLevel(20);
					MessageManager.getInstance().good(target, "You have been fed and healed by " + sender.getName() + "!");
					MessageManager.getInstance().good(sender, "Player " + target.getName() + " has been regenerated!");
					return true;
				}

				if (args.length > 2) {
					MessageManager.getInstance().severe(sender, "Too many arguments!");
					return true;
				}
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
		}

		return true;
	}
}
