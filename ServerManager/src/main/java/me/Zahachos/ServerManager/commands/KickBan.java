package me.Zahachos.ServerManager.commands;

import me.Zahachos.ServerManager.Main;
import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.managers.MessageManager;
import me.Zahachos.ServerManager.util.Cooldown;
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
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KickBan implements CommandExecutor, Listener {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player play = e.getPlayer();
        if (play.isBanned()) {
            if (Cooldown.isCooling(play.getUniqueId(), "TempBan")) {

                File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(player);
                List<String> list = config.getStringList("banned.temp.reason");
                String reason = list.get(list.size()-1);

                if (Cooldown.getTime(play.getUniqueId(), "TempBan") < 60000L) {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "" + ChatColor.BOLD + "You are still banned for " + Cooldown.getRemaining(play.getUniqueId(), "TempBan") + " seconds." + ChatColor.RED + " Reason: " + reason);
                    return;
                }

                if (Cooldown.getTime(play.getUniqueId(), "TempBan") < 3600000L) {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "" + ChatColor.BOLD + "You are still banned for " + Cooldown.getRemaining(play.getUniqueId(), "TempBan") + " minutes." + ChatColor.RED + " Reason: " + reason);
                    return;
                }

                if (Cooldown.getTime(play.getUniqueId(), "TempBan") < 86400000L) {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "" + ChatColor.BOLD + "You are still banned for " + Cooldown.getRemaining(play.getUniqueId(), "TempBan") + " hours." + ChatColor.RED + " Reason: " + reason);
                    return;
                }

                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "" + ChatColor.BOLD + "You are still banned for " + Cooldown.getRemaining(play.getUniqueId(), "TempBan") + " days." + ChatColor.RED + " Reason: " + reason);

            } else {
                File player = new File(Main.plugin.getDataFolder() + "/players/" + play.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(player);
                List<String> list = config.getStringList("banned.perm.reason");
                String reason = list.get(list.size()-1);
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.YELLOW + "" + ChatColor.BOLD + "You are permanentely banned! " + ChatColor.RED + "" + ChatColor.BOLD + "Reason: " + reason);
            }
        }
    }

    ConfigManager settings = ConfigManager.getInstance();

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (sender.hasPermission("sm.ban.tempban")) {
                if (args.length == 0) {
                    MessageManager.getInstance().severe(sender,"Please specify who you want to temporarily ban, the duration and the reason!");
                    return true;
                }
                if (args.length == 1) {
                    MessageManager.getInstance().severe(sender, "Please specify the duration and the reason of the temporary ban!");
                    return true;
                }
                if (args.length == 2) {
                    MessageManager.getInstance().severe(sender, "Please specify the reason of the temporary ban!");
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

                        if (playerFile.isSet("banned.temp.reason")) {
                            reason = playerFile.getStringList("banned.temp.reason");
                        }
                        reason.add(rs);
                        playerFile.set("banned.temp.reason", reason);
                        try {
                            playerFile.save(puuidFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        MessageManager.getInstance().custom(sender, ChatColor.GREEN + target.getName() + " has been temporarily banned! " + ChatColor.RED + "Reason: " + rs);
                        target.setBanned(true);

                        if (target.isOnline()) {
                            Player p = Bukkit.getServer().getPlayer(args[0]);

                            Cooldown.add(p.getUniqueId(), "TempBan", duration, System.currentTimeMillis());

                            if (Cooldown.getTime(p.getUniqueId(), "TempBan") < 60000L) {
                                p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily banned for " + Cooldown.getRemaining(p.getUniqueId(), "TempBan") + " seconds." + ChatColor.RED + " Reason: " + rs);
                                return true;
                            }
                            if (Cooldown.getTime(p.getUniqueId(), "TempBan") < 3600000L) {
                                p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily banned for " + Cooldown.getRemaining(p.getUniqueId(), "TempBan") + " minutes." + ChatColor.RED + " Reason: " + rs);
                                return true;
                            }
                            if (Cooldown.getTime(p.getUniqueId(), "TempBan") < 86400000L) {
                                p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily banned for " + Cooldown.getRemaining(p.getUniqueId(), "TempBan") + " hours." + ChatColor.RED + " Reason: " + rs);
                                return true;
                            }
                            p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been temporarily banned for " + Cooldown.getRemaining(p.getUniqueId(), "TempBan") + " days." + ChatColor.RED + " Reason: " + rs);
                            return true;
                        }
                        Cooldown.add(target.getUniqueId(), "TempBan", duration, System.currentTimeMillis());
                        return true;
                    }
                }
            }
            MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("pardon")) {
            if (sender.hasPermission("sm.ban.pardon")) {
                if (args.length == 0) {
                    MessageManager.getInstance().severe(sender, "Please specify who you want to pardon!");
                    return true;
                }
                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if (target == null) {
                    MessageManager.getInstance().severe(sender, "Player doesn't exist!");
                    return true;
                }
                if (!target.isBanned()) {
                    MessageManager.getInstance().severe(sender, "That player isn't banned!");
                    return true;
                }
                if (Cooldown.cooldownPlayers.containsKey(target.getUniqueId())) {
                    Cooldown.removeCooldown(target.getUniqueId(), "TempBan");
                    MessageManager.getInstance().good(sender, target.getName() + " is no longer temporarily banned!");
                    target.setBanned(false);
                    return true;
                }
                MessageManager.getInstance().good(sender, target.getName() + " is no longer permanently banned!");
                target.setBanned(false);
                return true;
            }
            MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender.hasPermission("sm.ban.ban")) {
                if (args.length == 0) {
                    MessageManager.getInstance().severe(sender, "Please specify who you want to permanently ban and the reason!");
                    return true;
                }
                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if (target == null) {
                    MessageManager.getInstance().severe(sender, "Player doesn't exist!");
                    return true;
                }
                if (args.length == 1) {
                    MessageManager.getInstance().severe(sender, "Please specify a reason for permanently banning that player!");
                    return true;
                }

                StringBuilder string = new StringBuilder();
                for (int r = 1; r < args.length; r++) {
                    string.append(args[r] + " ");
                }
                String rs = string.toString().trim();

                File puuidFile = new File(Main.plugin.getDataFolder() + "/players/" + target.getUniqueId() + ".yml");
                FileConfiguration playerFile = YamlConfiguration.loadConfiguration(puuidFile);

                List<String> reason = new ArrayList<String>();

                if (playerFile.isSet("banned.perm.reason")) {
                    reason = playerFile.getStringList("banned.perm.reason");
                }
                reason.add(rs);
                playerFile.set("banned.perm.reason", reason);
                try {
                    playerFile.save(puuidFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MessageManager.getInstance().good(sender, ChatColor.GREEN + target.getName() + " has been permanently banned! " + ChatColor.RED + "Reason: " + rs);

                if (target.isOnline()) {
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    p.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been permanently banned!" + ChatColor.RED + " Reason: " + rs);
                    target.setBanned(true);
                    return true;
                }
                target.setBanned(true);
                return true;
            }
            MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("kick")) {
            if (sender.hasPermission("sm.ban.kick")) {
                if (args.length == 0) {
                    MessageManager.getInstance().severe(sender, "Please specify who you want to kick and the reason!");
                    return true;
                }
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
                if (args.length == 1) {
                    MessageManager.getInstance().severe(sender, "Please specify a reason for the kick!");
                    return true;
                }
                StringBuilder string = new StringBuilder();
                for (int r = 1; r < args.length; r++) {
                    string.append(args[r] + " ");
                }
                String rs = string.toString().trim();

                File puuidFile = new File(Main.plugin.getDataFolder() + "/players/" + target.getUniqueId() + ".yml");
                FileConfiguration playerFile = YamlConfiguration.loadConfiguration(puuidFile);

                List<String> reason = new ArrayList<String>();

                if (playerFile.isSet("banned.kick.reason")) {
                    reason = playerFile.getStringList("banned.kick.reason");
                }
                reason.add(rs);
                playerFile.set("banned.kick.reason", reason);
                try {
                    playerFile.save(puuidFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                target.kickPlayer(ChatColor.YELLOW + "" + ChatColor.BOLD + "You have been kicked! " + ChatColor.RED + "Reason: " + rs);
                MessageManager.getInstance().custom(sender, ChatColor.GREEN + target.getName() + " has been successfully kicked! " + ChatColor.RED + "Reason: " + rs);
            }
            MessageManager.getInstance().severe(sender, "You don't have the permission to perform this command!");
        }
        return true;
    }
}