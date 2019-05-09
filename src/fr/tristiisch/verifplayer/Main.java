package fr.tristiisch.verifplayer;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.command.AlertCPSCommand;
import fr.tristiisch.verifplayer.command.VerifCommand;
import fr.tristiisch.verifplayer.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.gui.GuiManager;
import fr.tristiisch.verifplayer.gui.listener.GuiListener;
import fr.tristiisch.verifplayer.listener.VerifPlayerListener;
import fr.tristiisch.verifplayer.utils.Metrics;
import fr.tristiisch.verifplayer.utils.SpigotUpdater;
import fr.tristiisch.verifplayer.utils.VersionUtils.Versions;
import fr.tristiisch.verifplayer.utils.config.CustomConfig;
import fr.tristiisch.verifplayer.verifclick.PlayerClicksListener;
import fr.tristiisch.verifplayer.verifgui.listener.VerifGuiListener;
import fr.tristiisch.verifplayer.verifgui.listener.items.HeadListener;
import fr.tristiisch.verifplayer.verifgui.listener.items.InventoryListener;
import fr.tristiisch.verifplayer.verifgui.listener.items.InventoryListener1_7;
import fr.tristiisch.verifplayer.verifgui.listener.items.InventoryListener1_12;

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

		//this.saveDefaultConfig();
		CustomConfig.loadConfigs(this);
		//PermissionManager.createPermFile(this);

		this.getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		this.getCommand("verif").setExecutor(new VerifCommand());
		this.getCommand("alertcps").setExecutor(new AlertCPSCommand());

		final PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new VerifPlayerListener(), this);
		pluginManager.registerEvents(new GuiListener(), this);
		pluginManager.registerEvents(new PlayerClicksListener(), this);
		pluginManager.registerEvents(new VerifGuiListener(), this);

		pluginManager.registerEvents(new HeadListener(), this);
		pluginManager.registerEvents(new InventoryListener(), this);
		if(Versions.V1_12.isEqualOrOlder()) {
			pluginManager.registerEvents(new InventoryListener1_12(), this);
		} else {
			pluginManager.registerEvents(new InventoryListener1_7(), this);
		}

		new Metrics(this);
		new SpigotUpdater(this, 67212);
		console.sendMessage("§2" + this.getDescription().getName() + "§a by Tristiisch (v" + this.getDescription().getVersion() + ") is activated.");
	}
}
