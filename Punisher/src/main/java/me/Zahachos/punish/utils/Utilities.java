package me.Zahachos.punish.utils;

import me.Zahachos.punish.commands.Punish;
import me.Zahachos.punish.managers.ConfigManager;
import me.Zahachos.punish.managers.MessageManager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    static Utilities instance = new Utilities();

    public static Utilities getInstance() {
        return instance;
    }

    FileConfiguration config = ConfigManager.getInstance().getConfig();

    public String getInventoryName(int invID, Player p) {
        String name = config.getString(invID+".name");
        name = color(name, p);
        return name;
    }
    
    public int getInventoryID(String invName, Player p) {
        int counter = 1;
        while(config.get(counter + "") != null) {
            if (getInventoryName(counter, p).equals(ChatColor.stripColor(invName))) {
                return counter;
            } else {
                counter++;
            }
        }
        return 0;
    }

    public String getItemName(int invID, int itemID, Player p) {
        Bukkit.broadcastMessage("tested item name");
        String name = config.getString(invID+".items."+itemID+".name");
        name = color(name, p);
        return name.trim();
    }
    
    public int getItemID(int invID, String itemName, Player p) {
        int counter = 1;
        while (config.get(invID+".items."+counter) != null) {
            if (getItemName(invID, counter, p).equals(ChatColor.stripColor(itemName))) {
                return counter;
            } else {
                counter++;
            }
        }
        return 0;
    }

    public int getItemID(String itemName, Player p) {
        int counter = 1;
        while (config.get(counter+"") != null) {
            Bukkit.broadcastMessage(counter+" "+itemName);
            int counterItem = 1;
            while (config.get(counter+".items."+counterItem) != null) {
                Bukkit.broadcastMessage(counter+" "+counterItem+" "+(ChatColor.stripColor(getItemName(counter, counterItem, p))));
                if ((ChatColor.stripColor(getItemName(counter, counterItem, p))).equals(ChatColor.stripColor(itemName))) {
                    Bukkit.broadcastMessage("got it");
                    return counterItem;
                }
                counterItem ++;
            }
            counter++;
        }
        Bukkit.broadcastMessage("0");
        return 0;
    }
    
    public List<String> getLore(int inv, int itemID, Player p) {
        List<String> lore = config.getStringList(inv+".items."+itemID+".lore");
        int counterLore = 1;
        while (counterLore <= lore.size()) {
            String loreString = lore.get(counterLore - 1);
            loreString = color(loreString, p);
            lore.remove(counterLore-1);
            lore.add(counterLore-1, loreString);
            counterLore++;
        }
        return lore;
    }

    public ItemStack getPlayerSkull(String name) {
        Location l = Bukkit.getWorlds().get(0).getSpawnLocation();
        l.setY(0);
        Block b = l.getBlock();
        Material m = b.getType();
        byte data = b.getData();
        b.setType(Material.SKULL);
        Skull skull = (Skull) b.getState();
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwner(name);
        skull.update();
        ItemStack is = b.getDrops().iterator().next();
        b.setType(m);
        b.setData(data);
        return is;
    }

    public boolean hasPaid(String player, Player sender) {
        try {
            String URL = "http://www.minecraft.net/haspaid.jsp?user=" + player;
            Document playerExists = Jsoup.connect(URL).get();
            String inputLine = playerExists.select("body").toString();
            Pattern pattern = Pattern.compile("\\s([A-Za-z]+)");
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                if (matcher.group(1).equalsIgnoreCase("true")) {
                    return true;
                } else {
                    MessageManager.getInstance().severe(sender, "Player doesn't exist!");
                    return false;
                }
            }
        } catch (IOException e) {
            MessageManager.getInstance().severe(sender, "Connection timed out! Please try again!");
        }
        return false;
    }
    
    public String color(String string, Player p) {
        string = string.replace("&", "§").replace("%playername%", Punish.punishing.get(p).getName()).replace("%uuid%", Punish.punishing.get(p).getUniqueId().toString());
        return string.trim();
    }
}
