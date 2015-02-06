package me.Zahachos.punish.commands;

import me.Zahachos.punish.Utilities;
import me.Zahachos.punish.managers.ConfigManager;
import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

public class Punish implements CommandExecutor {

    public static Inventory inv;
    private ItemStack info, kick, mute, ban;
    public static OfflinePlayer playername;

    FileConfiguration config = ConfigManager.getInstance().getConfig();

    public void openGUI(Player player, int inventoryName) {

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
            while (config.get(inventoryName+".items."+counter) != null) {
                name = config.getString(inventoryName+".items." +counter+".name");
                name = name.replace("&", "§");
                m = Material.getMaterial((String) config.get(inventoryName+".items."+counter+".material"));
                ItemStack item = createItem(inventoryName, m, name, counter);
                int slot = config.getInt(inventoryName+".items."+counter+".slot");
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

    private ItemStack createItem(int inventoryName, Material m, String name, int number) {
        if (m.equals(Material.SKULL_ITEM)) {
            ItemStack itemstack = Utilities.getInstance().getPlayerSkull(playername.getName());
            SkullMeta im = (SkullMeta) itemstack.getItemMeta();
            im.setDisplayName(Utilities.getInstance().getItemName(inventoryName, number));
            im.setOwner(playername.getName());
            im.setLore(Utilities.getInstance().getLore(inventoryName, number));
            itemstack.setItemMeta(im);
            return itemstack;
        }
        ItemStack i = new ItemStack(m, 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(Utilities.getInstance().getItemName(inventoryName, number));
        im.setLore(Utilities.getInstance().getLore(inventoryName, number));
        i.setItemMeta(im);
        return i;
    }
    
    public static HashMap<Player, OfflinePlayer> punishing = new HashMap<>();

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
            
            if (Utilities.getInstance().hasPaid(args[0], player)) {
                punishing.put(player, Bukkit.getOfflinePlayer(args[0]));
                this.playername = Bukkit.getOfflinePlayer(args[0]);
                openGUI(player, 1);
            }
        }
        return true;
    }
}