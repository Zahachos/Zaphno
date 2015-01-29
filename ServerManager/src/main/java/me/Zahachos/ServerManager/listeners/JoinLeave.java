package me.Zahachos.ServerManager.listeners;

import me.Zahachos.ServerManager.apis.chatimage.ImageChar;
import me.Zahachos.ServerManager.apis.chatimage.ImageMessage;
import me.Zahachos.ServerManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;


public class JoinLeave implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);

		if (p.hasPlayedBefore()) {

			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getWorld().getName().equals("Hub") && player.getName() != p.getName()) {
					MessageManager.getInstance().custom(player, ChatColor.AQUA + "(" + ChatColor.DARK_AQUA + "+" + ChatColor.AQUA + ") " + ChatColor.BLUE + p.getName());
				}
			}
			try {
				URL playerHead = new URL("https://crafatar.com/avatars/" + e.getPlayer().getUniqueId() + "?helm");
				BufferedImage imageToSend = ImageIO.read(playerHead);
				new ImageMessage(imageToSend, 8, ImageChar.MEDIUM_SHADE.getChar()).appendText("", "",
						ChatColor.GOLD + "Welcome back to ZaphnoCraft " + p.getName() + "!", "",
						ChatColor.YELLOW + "Be sure to check out the help section",
						ChatColor.YELLOW + "in your inventory!"
						).sendToPlayer(p);
			} catch (Exception exe) {exe.printStackTrace();}
		} else {
			try {
				URL playerHead = new URL("https://crafatar.com/avatars/" + e.getPlayer().getUniqueId() + "?helm");
				BufferedImage imageToSend = ImageIO.read(playerHead);
				new ImageMessage(imageToSend, 8, ImageChar.MEDIUM_SHADE.getChar()).appendText("", "", "",
						ChatColor.GOLD + "" + ChatColor.BOLD + "Please warmly welcome the new player ",
						ChatColor.GOLD + "" + ChatColor.BOLD + p.getName() + " to ZaphnoCraft!"
						).sendToPlayer(p);
			} catch (Exception exe) {exe.printStackTrace();}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().getName().equals("Hub") && player.getName() != p.getName()) {
				MessageManager.getInstance().custom(player, ChatColor.YELLOW + "(" + ChatColor.RED + "-" + ChatColor.YELLOW + ") " + ChatColor.GOLD + p.getName());
			}
		}
	}
}
