package me.Zahachos.punish;

import me.Zahachos.punish.managers.MessageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punish implements CommandExecutor {

    public static Inventory inv;
    private ItemStack info, kick, mute, ban;
    public static OfflinePlayer playername;

    public void openGUI(Player p) {
        inv = Bukkit.getServer().createInventory(null, 9, "Choose a punishment type:");

        info = createItem(Material.SKULL_ITEM, "Player Info");
        kick = createItem(Material.GLOWSTONE_DUST, "Kick/Warn");
        mute = createItem(Material.SULPHUR, "Mute");
        ban = createItem(Material.REDSTONE, "Ban");

        inv.setItem(0, info);
        inv.setItem(3, kick);
        inv.setItem(5, mute);
        inv.setItem(7, ban);

        p.openInventory(inv);
        playername = null;
    }

    private ItemStack createItem(Material m, String name) {
        if (m.equals(Material.SKULL_ITEM)) {
            ItemStack itemstack = getPlayerSkull(name);
            SkullMeta im = (SkullMeta) itemstack.getItemMeta();
            im.setDisplayName(ChatColor.YELLOW + name);
            im.setOwner(playername.getName());
            im.setLore(Arrays.asList(ChatColor.GOLD + playername.getName(),
                    ChatColor.YELLOW + "" + playername.getUniqueId(),
                    ChatColor.RED + "Click the skull to see past infractions", ChatColor.RED
                            + "from this player."));
            itemstack.setItemMeta(im);
            return itemstack;
        }
        ItemStack i = new ItemStack(m, 1);
        if (name.equals("Mute") || name.equals("Kick/Warn")
                || name.equals("Ban")) {
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.DARK_RED + name);
            im.setLore(Arrays.asList(ChatColor.RED + "Click here to view "
                    + name.toLowerCase(), ChatColor.RED
                    + "punishment options."));
            i.setItemMeta(im);
            return i;
        }
        return null;
    }

    public static ItemStack getPlayerSkull(String name) {
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
                MessageManager.getInstance().severe(player, "Too many arguments!");
                return true;
            }
            try {
                String URL = "http://www.minecraft.net/haspaid.jsp?user=" + args[0];
                Document playerExists = Jsoup.connect(URL).get();
                String inputLine = playerExists.select("body").toString();
                Pattern pattern = Pattern.compile("\\s([A-Za-z]+)");
                Matcher matcher = pattern.matcher(inputLine);
                if (matcher.find()) {
                    if (matcher.group(1).equalsIgnoreCase("true")) {
                        playername = Bukkit.getServer().getOfflinePlayer(args[0]);
                    } else {
                        MessageManager.getInstance().severe(player, "Player doesn't exist!");
                        return true;
                    }
                }
            } catch (IOException e) {
                MessageManager.getInstance().severe(player, "Connection timed out! Please try again!");
                return true;
            }
            openGUI(player);
        }
        return true;
    }
}
