package me.Zahachos.punish;

import me.Zahachos.punish.events.PlayerBanClickEvent;
import me.Zahachos.punish.events.PlayerInfoClickEvent;
import me.Zahachos.punish.managers.ConfigManager;
import me.Zahachos.punish.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EventCaller implements Listener {

    Utilities utils = Utilities.getInstance();
    FileConfiguration config = ConfigManager.getInstance().getConfig();

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        if (!isPunishInventory(e.getInventory().getName(), (Player) e.getWhoClicked())) {
            return;
        }
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        int invID = utils.getInventoryID(e.getInventory().getName(), p);

        if (e.getClick().isLeftClick()) {
            try {

                if (getIfPlayerInfoAction(invID, utils.getItemID(e.getCurrentItem().getItemMeta().getDisplayName(), p))) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerInfoClickEvent(p));
                }

                if (getIfBanAction(invID, utils.getItemID(e.getCurrentItem().getItemMeta().getDisplayName(), p))) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerBanClickEvent(p, e.getInventory()));
                }

            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    public boolean isPunishInventory(String invName, Player p) {
        int counter = 1;
        while (config.get(counter + ".name") != null) {
            String name = ChatColor.stripColor(utils.getInventoryName(counter, p));
            Bukkit.broadcastMessage(invName + " " + name);
            if (name != null && name.equals(invName)) {
                return true;
            }
            counter++;
        }
        return false;
    }

    public boolean getIfBanAction(int invID, int itemID) {
        if (config.isSet(invID + ".items." + itemID + ".playerinfo")) {
            if (config.getBoolean(invID + ".items." + itemID + ".playerinfo")) {
                return true;
            }
        }
        return false;
    }

    public boolean getIfPlayerInfoAction(int invID, int itemID) {
        if (config.isSet(invID+".items."+itemID+".ban")) {
            if (config.getBoolean(invID + ".items." + itemID + ".ban")) { return true; }
        }
        return false;
    }
}