package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.Main;
import me.Zahachos.ServerManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Teleport implements CommandExecutor {

	public static HashMap<UUID, Integer> tpa = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> tph = new HashMap<UUID, Integer>();
	public static HashMap<UUID, UUID> tpaccept = new HashMap<UUID, UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("tp")) {

			if (sender.hasPermission("sm.tp.tp")) {

				if (args.length == 0) {
					MessageManager.getInstance().severe(sender, "Please specify a player!");
					return true;
				}
				if (args.length == 1) {

					if(!(sender instanceof Player)) {
						MessageManager.getInstance().severe(sender, "The console connot teleport! Specify a second player!");
						return true;
					}

					Player p = (Player) sender;

					Player target = null;
					for (Player test : Bukkit.getOnlinePlayers()) {
						if (test.getName().equals(args[0])) {
							target = test;
							break;
						}
					}

					if (target == null) {
						MessageManager.getInstance().severe(p, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					MessageManager.getInstance().good(p, "You have been teleported to " + target.getName());
					p.teleport(target);
					MessageManager.getInstance().info(target, p.getName() + " has teleported to you!");
					return true;
				}

				if (args.length == 2) {

					Player target = null;
					for (Player test : Bukkit.getOnlinePlayers()) {
						if (test.getName().equals(args[0])) {
							target = test;
							break;
						}
					}

					Player target1 = null;
					for (Player test1 : Bukkit.getOnlinePlayers()) {
						if (test1.getName().equals(args[1])) {
							target1 = test1;
							break;
						}
					}

					if (target == null || target1 == null) {
						sender.sendMessage(ChatColor.RED + "One of the players doesn't exist or isn't online!");
						return true;
					}

					target.teleport(target1);
					MessageManager.getInstance().info(target, target1.getName() + " was teleported to you by " + sender.getName() + ".");
					MessageManager.getInstance().info(target1, "You were teleported to " + target.getName() + " by " + sender.getName() + ".");
					MessageManager.getInstance().custom(sender, ChatColor.GREEN + target.getName() + " has been teleported to " + target1.getName() + ".");
				}

				if (args.length > 2) {
					MessageManager.getInstance().severe(sender, "Too many arguments!");
					return true;
				}
			}
			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
			return true;

		}

		if (cmd.getName().equalsIgnoreCase("tpall")) {

			if (sender.hasPermission("sm.tp.tpall")) {

				if (!(sender instanceof Player)) {
					MessageManager.getInstance().severe(sender, "The console can't teleport players to him!");
					return true;
				}

				if (args.length > 0) {
					MessageManager.getInstance().severe(sender, "Too many arguments!");
					return true;
				}

				Player p = (Player) sender;

				for (Player player : Bukkit.getOnlinePlayers()) {
					player.teleport(p);
					MessageManager.getInstance().info(player, p.getName() + " has teleported everyone to him.");
				}

				MessageManager.getInstance().good(p, "Everyone one the server was teleported to you!");
				return true;
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("tpa")) {

			if (sender.hasPermission("sm.tp.tpa")) {

				if (!(sender instanceof Player)) {
					MessageManager.getInstance().severe(sender, "The console cannot teleport!");
					return true;
				}

				Player p = (Player) sender;

				if (args.length == 0) {
					MessageManager.getInstance().severe(p, "Please specify a player!");
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
						MessageManager.getInstance().severe(p, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					tpa.put(target.getUniqueId(), 60);
					tpaccept.put(target.getUniqueId(), p.getUniqueId());

					if (tpa.size() == 1) {

						new BukkitRunnable() {

							@Override
							public void run() {
								if (!tpa.isEmpty()) {
									for (UUID uuid : tpa.keySet()) {
										Player player = Bukkit.getPlayer(uuid);
										tpa.put(uuid, tpa.get(uuid) - 1);
										if (tpa.get(uuid) == 10) {
											if (player.isOnline()) {
												MessageManager.getInstance().info(player, "Your teleport request will expire in 10 seconds!");
											}
										}
										if (tpa.get(uuid) == 0) {
											if (player.isOnline()) {
												MessageManager.getInstance().custom(player, "The teleport request from " + Bukkit.getPlayer(uuid).getName() + " has expired!");
											}
											tpa.remove(uuid);
											tpaccept.remove(tpaccept.values().remove(uuid));
											if (tpa.isEmpty()) {
												this.cancel();
											}
										}
									}
								} else {
									this.cancel();
								}
							}
						}.runTaskTimer(Main.plugin, 20L, 20L);

					}
					MessageManager.getInstance().custom(target, ChatColor.YELLOW + "Player " + ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " has requested to teleport to you! Use  " + ChatColor.YELLOW + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + ChatColor.YELLOW + " to teleport him!");
					return true;
				}

				MessageManager.getInstance().severe(p, "Too many arguments!");
				return true;
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tph")) {

			if (sender.hasPermission("sm.tp.tph")) {

				if (!(sender instanceof Player)) {
					MessageManager.getInstance().severe(sender, "The console cannot teleport!");
					return true;
				}

				Player p = (Player) sender;

				if (args.length == 0) {
					MessageManager.getInstance().severe(p, "Please specify a player!");
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
						MessageManager.getInstance().severe(p, "Either that player doesn't exist or he isn't online!");
						return true;
					}

					tph.put(target.getUniqueId(), 60);
					tpaccept.put(target.getUniqueId(), p.getUniqueId());

					if (tph.size() == 1) {

						new BukkitRunnable() {

							@Override
							public void run() {
								if (!tph.isEmpty()) {
									for (UUID uuid : tph.keySet()) {
										Player player = Bukkit.getPlayer(uuid);
										tph.put(uuid, tph.get(uuid) - 1);
										if (tph.get(uuid) == 10) {
											if (player.isOnline()) {
												MessageManager.getInstance().info(player, "Your teleport request will expire in 10 seconds!");
											}
										}
										if (tph.get(uuid) == 0) {
											if (player.isOnline()) {
												MessageManager.getInstance().custom(player, "The teleport request from " + Bukkit.getPlayer(uuid).getName() + " has expired!");
											}
											tph.remove(uuid);
											tpaccept.remove(tpaccept.values().remove(uuid));
											if (tph.isEmpty()) {
												this.cancel();
											}
										}
									}
								} else {
									this.cancel();
								}
							}
						}.runTaskTimer(Main.plugin, 20L, 20L);

					}
					MessageManager.getInstance().custom(target, ChatColor.YELLOW + "Player " + ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " has requested that you teleport to him! Use  " + ChatColor.YELLOW + ChatColor.BOLD + "/tpaccept" + ChatColor.RESET + ChatColor.YELLOW + " to teleport to him!");
					return true;
				}

				MessageManager.getInstance().severe(p, "Too many arguments!");
				return true;
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("tpaccept")) {

			if (sender.hasPermission("sm.tp.tpa")) {

				if (!(sender instanceof Player)) {
					MessageManager.getInstance().severe(sender, "The console cannot teleport!");
					return true;
				}

				Player p = (Player) sender;

				if (args.length == 0) {
					if (tpa.containsKey(p.getUniqueId()) || tpaccept.containsKey(p.getUniqueId())) {
						Player target = Bukkit.getPlayer(tpaccept.get(p.getUniqueId()));
						target.teleport(p);
						tpaccept.remove(p.getUniqueId());
						tpa.remove(p.getUniqueId());
						return true;
					} else if (tph.containsKey(p.getUniqueId()) || tpaccept.containsKey(p.getUniqueId())) {
						Player target = Bukkit.getPlayer(tpaccept.get(p.getUniqueId()));
						p.teleport(target);
						tpaccept.remove(p.getUniqueId());
						tpa.remove(p.getUniqueId());
						return true;
					} else {
						MessageManager.getInstance().severe(p, "You don't have any active requests!");
						return true;
					}
				}

				MessageManager.getInstance().severe(p, "Too many arguments!");
				return true;
			}

			MessageManager.getInstance().severe(sender, "Unfortunately you can't do that.");
			return true;
		}
		return true;
	}
}