package me.Zahachos.punish;

import me.Zahachos.punish.events.PlayerBanClickEvent;
import me.Zahachos.punish.events.PlayerInfoClickEvent;
import me.Zahachos.punish.managers.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EventCaller implements Listener {

	private Player p;
    FileConfiguration config = ConfigManager.getInstance().getConfig();

	@EventHandler
	public void OnInventoryClick(InventoryClickEvent e) {
		if(!isPunishInventory(ChatColor.stripColor(e.getInventory().getName()))) { return; }
		e.setCancelled(true);
		if (e.getClick().isLeftClick()) {
			p = (Player) e.getWhoClicked();
			try {
                
				if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					Bukkit.broadcastMessage(e.getSlotType().name());
					Bukkit.getServer().getPluginManager().callEvent(new PlayerInfoClickEvent(p));
				}
                
                if (getBanAction(getItemID(e.getCurrentItem(), e.getInventory().getName()), getInventory(e.getInventory().getName()))) {
                    Bukkit.broadcastMessage("test");
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerBanClickEvent(p, e.getInventory()));
                }
                
			} catch(Exception exe) {}
		}
	}
    
    public int getItemID(ItemStack i, String invName) {
        int counter = 1;
        int inv = getInventory(invName);
        while(config.get(inv+".items."+counter) != null) {
            if (i.getItemMeta().getDisplayName().equals(config.getString(inv+".items."+counter))) {
                return counter;
            }
            counter++;
        }
        return 0;
    }
    
    public int getInventory(String inv) {
        int counter = 1;
        while (config.get(counter+".name") != null) {
            String invName = config.getString(counter+".name");
            if (invName.equals(inv)) {
                return counter;
            }
            counter ++;
        }
        return 0;
    }
    
    public boolean getBanAction(int itemID, int inv) {
        if (!config.contains(inv+".items."+itemID+".ban")) { return false; }
        return config.getBoolean(inv + ".items." + itemID + ".ban") == true;
    }
    
    public boolean isPunishInventory(String invName) {
        int counter = 1;
        while(config.get(counter+".name") != null) {
            String name = ChatColor.stripColor(Utilities.getInstance().getInventoryName(counter));
            if (name.equals(invName)) {
                return true;
            }
            counter ++;
        }
        return false;
    }
}