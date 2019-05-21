package fr.tristiisch.verifplayer;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.core.command.AlertCpsCommand;
import fr.tristiisch.verifplayer.core.command.VerifCommand;
import fr.tristiisch.verifplayer.core.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.core.verifclick.PlayerClicksListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.VerifGuiListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.HeadListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener1_11;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener1_12;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecCommand;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecConfigListener;
import fr.tristiisch.verifplayer.gui.GuiManager;
import fr.tristiisch.verifplayer.gui.listener.GuiListener;
import fr.tristiisch.verifplayer.hook.HookHandler;
import fr.tristiisch.verifplayer.listener.VerifPlayerListener;
import fr.tristiisch.verifplayer.utils.Metrics;
import fr.tristiisch.verifplayer.utils.SpigotUpdater;
import fr.tristiisch.verifplayer.utils.VersionUtils;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.CustomConfig;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public String getPrefix() {
		return "§b[§3" + this.getDescription().getName() + "§b] ";
	}

	@Override
	public void onDisable() {
		GuiManager.closeAll();
		this.sendMessage("§4" + this.getDescription().getName() + "§c by Tristiisch (" + this.getDescription().getVersion() + ") is disabled.");
	}

	@Override
	public void onEnable() {
		instance = this;
		final PluginManager pluginManager = this.getServer().getPluginManager();
		CustomConfig.loadConfigs(this);
		new SpigotUpdater(this, 67212);
		new VersionUtils(this);

		this.getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		this.getCommand("verif").setExecutor(new VerifCommand());
		this.getCommand("alertcps").setExecutor(new AlertCpsCommand());
		// this.getCommand("vanish").setExecutor(new VanishCommand());
		this.getCommand("verifspec").setExecutor(new VerifSpecCommand());
		// this.getCommand("freeze").setExecutor(new FreezeCommand());

		pluginManager.registerEvents(new VerifPlayerListener(), this);
		pluginManager.registerEvents(new GuiListener(), this);
		pluginManager.registerEvents(new PlayerClicksListener(), this);
		pluginManager.registerEvents(new VerifGuiListener(), this);

		pluginManager.registerEvents(new HeadListener(), this);
		pluginManager.registerEvents(new InventoryListener(), this);
		pluginManager.registerEvents(new VerifSpecConfigListener(), this);
		// pluginManager.registerEvents(new FreezeListener(), this);
		// pluginManager.registerEvents(new VerifSpecListener(), this);
		// pluginManager.registerEvents(new VerifSpecRestrictListener(), this);
		// pluginManager.registerEvents(new VanishListener(), this);
		if(ServerVersion.V1_12.isEqualOrOlder()) {
			// 	pluginManager.registerEvents(new VanishListener1_12(), this);
			pluginManager.registerEvents(new InventoryListener1_12(), this);
		} else {
			// 	pluginManager.registerEvents(new VanishListener1_7(), this);
			pluginManager.registerEvents(new InventoryListener1_11(), this);
		}
		new HookHandler(this);
		new Metrics(this);
		GuiManager.closeAll();
		// DirectoryWatcher.start(this);
		this.sendMessage("§2" + this.getDescription().getName() + "§a by Tristiisch (" + this.getDescription().getVersion() + ") is activated.");
	}

	public void sendMessage(final String message) {
		this.getServer().getConsoleSender().sendMessage(this.getPrefix() + message);
	}
}
