package me.Zahachos.punish;

import me.Zahachos.punish.cooldowns.Cooldown;
import me.Zahachos.punish.cooldowns.utilReloadSave;
import me.Zahachos.punish.listeners.ClickPlayerInfo;
import me.Zahachos.punish.listeners.IfBanned;
import me.Zahachos.punish.listeners.IfMuted;
import me.Zahachos.punish.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main plugin = null;
	
	@Override
	public void onEnable() {
		ConfigManager.getInstance().setup(this);
		
		getCommand("punish").setExecutor(new Punish());
		Bukkit.getPluginManager().registerEvents(new IfBanned(), this);
		Bukkit.getPluginManager().registerEvents(new IfMuted(), this);
		Bukkit.getPluginManager().registerEvents(new EventCaller(), this);
		Bukkit.getPluginManager().registerEvents(new ClickPlayerInfo(), this);
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
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Has been disabled!");
		
		utilReloadSave.saveMutes();
		utilReloadSave.saveBans();
		
	}
}