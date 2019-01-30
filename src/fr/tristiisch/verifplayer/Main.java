package fr.tristiisch.verifplayer;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.command.AlertCPSCommand;
import fr.tristiisch.verifplayer.command.VerifCommand;
import fr.tristiisch.verifplayer.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.listener.FastClickListener;
import fr.tristiisch.verifplayer.listener.VerifPlayerListener;
import fr.tristiisch.verifplayer.runnable.FastClickRunnable;
import fr.tristiisch.verifplayer.runnable.VerifRunnable;
import fr.tristiisch.verifplayer.utils.Metrics;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage("§4" + this.getDescription().getName() + "§c by Tristiisch (v" + this.getDescription().getVersion() + ") is disabled.");
	}

	@Override
	public void onEnable() {
		instance = this;
		final ConsoleCommandSender console = Bukkit.getConsoleSender();

		this.saveDefaultConfig();

		this.getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		this.getCommand("verif").setExecutor(new VerifCommand());
		this.getCommand("alertcps").setExecutor(new AlertCPSCommand());

		final PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new FastClickListener(), this);
		pluginManager.registerEvents(new VerifPlayerListener(), this);

		// 1 sec
		new FastClickRunnable().runTaskTimerAsynchronously(this, 0, 20);
		// 0.05 sec
		new VerifRunnable().runTaskTimerAsynchronously(this, 0, 1);

		new Metrics(this);
		//new SpigotUpdater(this, 0);
		System.out.println(Bukkit.getServer().getClass().getPackage().getName());
		console.sendMessage("§2" + this.getDescription().getName() + "§a by Tristiisch (v" + this.getDescription().getVersion() + ") is activated.");
		System.out.println("test: " + Bukkit.getServer().getClass().getPackage().getName());
	}
}
