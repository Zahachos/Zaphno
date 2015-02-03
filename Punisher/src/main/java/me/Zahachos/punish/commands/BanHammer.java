package me.Zahachos.punish.commands;

import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BanHammer implements CommandExecutor, Listener {
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] strings) {
        if (!(sender instanceof Player)) {
            MessageManager.getInstance().severe(sender, "This is a player-only command!");
            return true;
        }
        
        Player p = (Player) sender;
        
        ItemsStack i = new ItemStack(Material.BLAZE_ROD, 1);
        7
        
        if (p.getInventory().contains()
        
        return true;
    }
}
