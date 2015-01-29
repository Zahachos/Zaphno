package me.Zahachos.ServerManager;

import me.Zahachos.ServerManager.commands.*;
import me.Zahachos.ServerManager.listeners.JoinLeave;
import me.Zahachos.ServerManager.listeners.OnDeath;
import me.Zahachos.ServerManager.listeners.OnReconnect;
import me.Zahachos.ServerManager.managers.ConfigManager;
import me.Zahachos.ServerManager.util.Cooldown;
import me.Zahachos.ServerManager.util.utilReloadSave;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Main plugin = null;
	
	@Override
	public void onEnable() {
		ConfigManager.getInstance().setup(this);
		
		getCommand("heal").setExecutor(new HealFeed());
		getCommand("feed").setExecutor(new HealFeed());
		getCommand("regenerate").setExecutor(new HealFeed());
		getCommand("tp").setExecutor(new Teleport());
		getCommand("tpall").setExecutor(new Teleport());
		getCommand("tpa").setExecutor(new Teleport());
		getCommand("tph").setExecutor(new Teleport());
		getCommand("tpaccept").setExecutor(new Teleport());
		getCommand("freeze").setExecutor(new Freeze());
		getCommand("warp").setExecutor(new Warp());
		getCommand("setwarp").setExecutor(new Warp());
		getCommand("delwarp").setExecutor(new Warp());
		getCommand("unmute").setExecutor(new Mute());
		getCommand("mute").setExecutor(new Mute());
		getCommand("tempmute").setExecutor(new Mute());
		getCommand("tempban").setExecutor(new KickBan());
		getCommand("pardon").setExecutor(new KickBan());
		getCommand("ban").setExecutor(new KickBan());
		getCommand("kick").setExecutor(new KickBan());
		getCommand("spawn").setExecutor(new Spawn());
		getCommand("setspawn").setExecutor(new Spawn());
		Bukkit.getPluginManager().registerEvents(new JoinLeave(), this);
		Bukkit.getPluginManager().registerEvents(new Freeze(), this);
		Bukkit.getPluginManager().registerEvents(new OnReconnect(), this);
		Bukkit.getPluginManager().registerEvents(new KickBan(), this);
		Bukkit.getPluginManager().registerEvents(new Mute(), this);
		Bukkit.getPluginManager().registerEvents(new OnDeath(), this);
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Has been enabled!");
		plugin = this;
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				Cooldown.handleCooldowns();
			}
		}, 1L, 1L);
		
		utilReloadSave.loadBans();
		utilReloadSave.loadMutes();
	}

	@Override
	public void onDisable() {
		Bukkit.getServer().getLogger().info("Basic is Disabled!");
		
		utilReloadSave.saveMutes();
		utilReloadSave.saveBans();
		
	}
}
