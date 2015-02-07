package me.Zahachos.punish.utils;

import me.Zahachos.punish.commands.Punish;
import me.Zahachos.punish.managers.ConfigManager;
import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
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

    public String getInventoryName(int number) {
        String name = config.getString(number+".name");
        try {
            name = name.replace("&", "§").replace("%playername%", Punish.playername.getName()).replace("%uuid%", Punish.playername.getUniqueId().toString());
        } catch(NullPointerException npe) {return name;}
        return null;
    }
    
    public int getInventoryID(String invName) {
        int counter = 1;
        while(config.getString(counter+".name") != null) {
            String name = getInventoryName(counter);
        }
        return 0;
    }

    public int getItemID(String name, int invID) {
        int counter = 1;
        while (config.get)
        //TODO: getItemID Method (while loop)
        return 0;
    }

    public String getItemName(int inv, int itemID) {
        String name = config.getString(inv+".items."+itemID+".name");
        name = name.replace("&", "§").replace("%playername%", Punish.playername.getName()).replace("%uuid%", Punish.playername.getUniqueId().toString());
        return name;
    }

    public List<String> getLore(int inv, int itemID) {
        List<String> lore = config.getStringList(inv+".items."+itemID+".lore");
        int counterLore = 1;
        while (counterLore <= lore.size()) {
            String loreString = lore.get(counterLore-1).replace("&", "§").replace("%playername%", Punish.playername.getName()).replace("%uuid%", Punish.playername.getUniqueId().toString());
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
    
}
