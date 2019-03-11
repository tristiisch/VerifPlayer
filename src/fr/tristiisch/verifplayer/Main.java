package fr.tristiisch.verifplayer;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.command.AlertCPSCommand;
import fr.tristiisch.verifplayer.command.VerifCommand;
import fr.tristiisch.verifplayer.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.listener.VerifPlayerListener;
import fr.tristiisch.verifplayer.utils.Metrics;
import fr.tristiisch.verifplayer.utils.gui.GuiManager;
import fr.tristiisch.verifplayer.utils.gui.listener.GuiListener;
import fr.tristiisch.verifplayer.utils.permission.PermissionManager;
import fr.tristiisch.verifplayer.verifclick.FastClickRunnable;
import fr.tristiisch.verifplayer.verifclick.PlayerClicksListener;
import fr.tristiisch.verifplayer.verifgui.listener.VerifGuiListener;
import fr.tristiisch.verifplayer.verifgui.listener.items.HeadListener;
import fr.tristiisch.verifplayer.verifgui.runnable.VerifGuiRunnable;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		GuiManager.closeAll();
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage("§4" + this.getDescription().getName() + "§c by Tristiisch (v" + this.getDescription().getVersion() + ") is disabled.");
	}

	@Override
	public void onEnable() {
		instance = this;
		final ConsoleCommandSender console = Bukkit.getConsoleSender();

		this.saveDefaultConfig();
		PermissionManager.createPermFile(this);

		this.getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		this.getCommand("verif").setExecutor(new VerifCommand());
		this.getCommand("alertcps").setExecutor(new AlertCPSCommand());

		final PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new VerifPlayerListener(), this);
		pluginManager.registerEvents(new GuiListener(), this);
		pluginManager.registerEvents(new PlayerClicksListener(), this);
		pluginManager.registerEvents(new VerifGuiListener(), this);

		pluginManager.registerEvents(new HeadListener(), this);

		// 1 sec
		new FastClickRunnable().runTaskTimerAsynchronously(this, 0, 20);
		VerifGuiRunnable.start();

		new Metrics(this);
		//new SpigotUpdater(this, 0);
		System.out.println(Bukkit.getServer().getClass().getPackage().getName());
		console.sendMessage("§2" + this.getDescription().getName() + "§a by Tristiisch (v" + this.getDescription().getVersion() + ") is activated.");
		System.out.println("test: " + Bukkit.getServer().getClass().getPackage().getName());
	}
}
