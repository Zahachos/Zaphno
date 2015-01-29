package me.Zahachos.ServerManager.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MessageManager {

	static MessageManager instance = new MessageManager();

	public static MessageManager getInstance() {
		return instance;
	}

	public void custom(Player p, String msg) {
		p.sendMessage(msg);
	}

	public void info(Player p, String msg) {
		p.sendMessage(ChatColor.YELLOW + msg);
	}

	public void severe(Player p, String msg) {
		p.sendMessage(ChatColor.RED + msg);
	}

	public void good(Player p, String msg) {
		p.sendMessage(ChatColor.GREEN + msg);
	}
	
	
	public void custom(CommandSender p, String msg) {
		p.sendMessage(msg);
	}

	public void info(CommandSender p, String msg) {
		p.sendMessage(ChatColor.YELLOW + msg);
	}

	public void severe(CommandSender p, String msg) {
		p.sendMessage(ChatColor.RED + msg);
	}

	public void good(CommandSender p, String msg) {
		p.sendMessage(ChatColor.GREEN + msg);
	}

}
