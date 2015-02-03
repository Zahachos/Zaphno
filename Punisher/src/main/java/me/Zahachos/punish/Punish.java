package me.Zahachos.punish;

import me.Zahachos.punish.managers.ConfigManager;
import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punish implements CommandExecutor {

    public static Inventory inv;
    private ItemStack info, kick, mute, ban;
    public static OfflinePlayer playername;

    FileConfiguration config = ConfigManager.getInstance().getConfig();

    public void openGUI(Player player, String inventoryName) {

        try {
            inv = Bukkit.getServer().createInventory(null, (int) config.get("main.size"), (String) config.get("main.name"));
        } catch (Exception exe) {
            MessageManager.getInstance().severe(player, "Error! The plugin isn't configured correctly!");
            return;
        }

        int counterTest = 1;
        while (config.isSet("main.items."+counterTest)) {
            counterTest ++;
        }

        if (counterTest > 9) {
            MessageManager.getInstance().severe(player, "Error! The config contains too many items to include!");
            return;
        }

        int counter = 1;
        String name;
        Material m;
        try {
            while (config.get("main.items."+counter) != null) {
                name = config.getString("main.items." +counter+".name");
                name = name.replace("&", "§");
                m = Material.getMaterial((String) config.get("main.items."+counter+".material"));
                ItemStack item = createItem(inventoryName, m, name, counter);
                int slot = config.getInt("main.items."+counter+".slot");
                inv.setItem(slot, item);
                counter++;
            }
        } catch(Exception exe) {
            MessageManager.getInstance().severe(player, "Error! The items in the config aren't properly set!");
            exe.printStackTrace();
            return;
        }

        player.openInventory(inv);
        playername = null;
    }

    private ItemStack createItem(String inventoryName, Material m, String name, int number) {
        if (m.equals(Material.SKULL_ITEM)) {
            ItemStack itemstack = getPlayerSkull(playername.getName());
            SkullMeta im = (SkullMeta) itemstack.getItemMeta();
            im.setDisplayName(getName(inventoryName, number));
            im.setOwner(playername.getName());
            im.setLore(getLore(inventoryName, number));
            itemstack.setItemMeta(im);
            return itemstack;
        }
        ItemStack i = new ItemStack(m, 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(getName(inventoryName, number));
        im.setLore(getLore(inventoryName, number));
        i.setItemMeta(im);
        return i;
    }
    
    public String getName(String inv, int itemID) {
        String name = config.getString(inv+".items."+itemID+".name");
        name = name.replace("&", "§").replace("%playername%", playername.getName()).replace("%uuid%", playername.getUniqueId().toString());
        return name;
    }
    
    public List<String> getLore(String inv, int itemID) {
        List<String> lore = config.getStringList(inv+".items."+itemID+".lore");
        int counterLore = 1;
        while (counterLore <= lore.size()) {
            String loreString = lore.get(counterLore-1).replace("&", "§").replace("%playername%", playername.getName()).replace("%uuid%", playername.getUniqueId().toString());
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("punish")) {
            if (!(sender instanceof Player)) {
                MessageManager.getInstance().severe(sender, "This is a player-only command!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                MessageManager.getInstance().severe(player, "Please specify who you would like to punish!");
                return true;
            }

            if (args.length > 1) {
                MessageManager.getInstance().info(player, "No reason specified, using the config default.");
            }
            
            if (hasPaid(args[0], player)) {
                this.playername = Bukkit.getOfflinePlayer(args[0]);
                openGUI(player, "main");
            }
        }
        return true;
    }
}