package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.managers.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warp")) {
            if (sender.hasPermission("sm.warp.warp")) {
                if (args.length == 2) {
                    Player target = null;
                    for (Player test : Bukkit.getOnlinePlayers()) {
                        if (test.getName().equals(args[1])) {
                            target = test;
                            break;
                        }
                    }
                    if (!(target == null)) {
                        if (ConfigManager.getInstance().getData().getString("warps." + args[0]) == null) {
                            MessageManager.getInstance().severe(sender, "That warp is not yet defined!");
                            return true;
                        }
                        MessageManager.getInstance().good(sender, target.getName() + " has been warped to " + args[0] + "!");
                        MessageManager.getInstance().good(target, sender.getName() + " has warped you to " + args[0] + "!");
                        Location location = new Location(Bukkit.getWorld(ConfigManager.getInstance().getData().getString("warps." + args[0]+ ".world")), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".x"), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".y"), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".z"));
                        location.setPitch((float) ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".pitch"));
                        location.setYaw((float) ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".yaw"));
                        target.teleport(location);
                        return true;
                    }
                    MessageManager.getInstance().severe(sender, "Either that player doesn't exist or he isn't online!");
                    return true;
                }
                if (!(sender instanceof Player)) {
                    MessageManager.getInstance().severe(sender, "Only players can use this command!");
                    return true;
                }

                Player p = (Player) sender;

                if (args.length == 0) {
                    MessageManager.getInstance().severe(sender, "Please specify where you want to go!");
                    return true;
                }
                if (args.length == 1) {
                    if (ConfigManager.getInstance().getData().getString("warps." + args[0]) == null) {
                        MessageManager.getInstance().severe(sender, "That warp is not yet defined!");
                        return true;
                    }
                    Location location = new Location(Bukkit.getWorld(ConfigManager.getInstance().getData().getString("warps." + args[0]+ ".world")), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".x"), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".y"), ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".z"));
                    location.setPitch((float) ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".pitch"));
                    location.setYaw((float) ConfigManager.getInstance().getData().getDouble("warps." + args[0]+ ".yaw"));
                    p.teleport(location);
                    MessageManager.getInstance().good(p, "You have been warped to " + args[0] + "!");
                    return true;
                }
                MessageManager.getInstance().severe((Player) sender, "You don't have the permission to perform this command!");
            }
            if (cmd.getName().equalsIgnoreCase("setwarp")) {
                if (sender.hasPermission("sm.warp.set")) {
                    if (!(sender instanceof Player)) {
                        MessageManager.getInstance().severe(sender, "Only players can use this command!");
                        return true;
                    }

                    Player p = (Player) sender;

                    if (args.length == 0) {
                        MessageManager.getInstance().severe(p, "Please specify the name of your warp!");
                        return true;
                    }
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".world", p.getLocation().getWorld().getName());
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".x", p.getLocation().getX());
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".y", p.getLocation().getY());
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".z", p.getLocation().getZ());
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".yaw", p.getLocation().getYaw());
                    ConfigManager.getInstance().getData().set("warps." + args[0] + ".pitch", p.getLocation().getPitch());
                    ConfigManager.getInstance().saveData();
                    MessageManager.getInstance().good(p, "The warp " + args[0] + " has been saved! Use /warp " + args[0] + " to teleport to it!");
                    return true;
                }
                MessageManager.getInstance().severe((Player) sender, "You don't have the permission to perform this command!");
            }

            if (cmd.getName().equalsIgnoreCase("delwarp")) {
                if (sender.hasPermission("sm.warp.delete")) {
                    if (args.length == 0) {
                        MessageManager.getInstance().severe((Player) sender, "Please specify which warp you would like to delete!");
                        return true;
                    }
                    if (!ConfigManager.getInstance().getData().isSet("warps." + args[0])) {
                        MessageManager.getInstance().severe((Player) sender, "That warp doesn't exist!");
                        return true;
                    }
                    ConfigManager.getInstance().getData().set("warps." + args[0], null);
                    ConfigManager.getInstance().saveData();
                    MessageManager.getInstance().good((Player) sender, "The warp " + args[0] + " has been deleted!");
                    return true;
                }
                MessageManager.getInstance().severe((Player) sender, "You don't have the permission to perform this command!");
            }
        }
        return true;
    }
}