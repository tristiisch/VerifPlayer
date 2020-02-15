package fr.tristiisch.verifplayer;

import org.bukkit.plugin.PluginManager;

import fr.tristiisch.verifplayer.core.alert.AlertCpsCommand;
import fr.tristiisch.verifplayer.core.alert.AlertCpsListener;
import fr.tristiisch.verifplayer.core.command.VerifCommand;
import fr.tristiisch.verifplayer.core.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.core.freeze.FreezeCommand;
import fr.tristiisch.verifplayer.core.freeze.FreezeListener;
import fr.tristiisch.verifplayer.core.vanish.commands.VanishCommand;
import fr.tristiisch.verifplayer.core.vanish.listeners.VanishListener;
import fr.tristiisch.verifplayer.core.vanish.listeners.VanishListener1_11;
import fr.tristiisch.verifplayer.core.vanish.listeners.VanishListener1_12;
import fr.tristiisch.verifplayer.core.verifclick.PlayerClicksListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.VerifGuiListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.HeadListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener1_11;
import fr.tristiisch.verifplayer.core.verifgui.listener.items.InventoryListener1_12;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecCommand;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecConfigListener;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecRestrictListener;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecToolsListener;
import fr.tristiisch.verifplayer.gui.listener.GuiListener;
import fr.tristiisch.verifplayer.hook.HookHandler;
import fr.tristiisch.verifplayer.hook.VanishHook;
import fr.tristiisch.verifplayer.listener.VerifPlayerListener;
import fr.tristiisch.verifplayer.utils.Metrics;
import fr.tristiisch.verifplayer.utils.SpigotUpdater;
import fr.tristiisch.verifplayer.utils.VersionUtils;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.CustomConfigs;

public class VerifPlayer extends VerifPlayerPlugin {

	@Override
	public void onDisable() {
		this.getVerifGuiHandler().closeAll();
		this.sendMessage("§4" + this.getDescription().getName() + "§c by Tristiisch (" + this.getDescription().getVersion() + ") is disabled.");
	}

	@Override
	public void onEnable() {
		this.getVerifGuiHandler().closeAll();
		PluginManager pluginManager = this.getServer().getPluginManager();

		pluginManager.registerEvents(new VerifSpecConfigListener(), this);

		new CustomConfigs(this);
		new SpigotUpdater(this, 67212);
		new VersionUtils(this);
		new HookHandler(this);

		this.getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		this.getCommand("verif").setExecutor(new VerifCommand());
		this.getCommand("alertcps").setExecutor(new AlertCpsCommand());

		this.getCommand("verifspec").setExecutor(new VerifSpecCommand());
		this.getCommand("freeze").setExecutor(new FreezeCommand());
		this.getCommand("verifvanish").setExecutor(new VanishCommand());

		pluginManager.registerEvents(new VanishListener(), this);

		pluginManager.registerEvents(new AlertCpsListener(), this);
		pluginManager.registerEvents(new VerifPlayerListener(), this);
		pluginManager.registerEvents(new GuiListener(), this);
		pluginManager.registerEvents(new PlayerClicksListener(), this);
		pluginManager.registerEvents(new VerifGuiListener(), this);
		pluginManager.registerEvents(new HeadListener(), this);
		pluginManager.registerEvents(new InventoryListener(), this);

		pluginManager.registerEvents(new FreezeListener(), this);
		pluginManager.registerEvents(new VerifSpecToolsListener(), this);
		pluginManager.registerEvents(new VerifSpecRestrictListener(), this);

		VanishHook vanishHook = VanishHook.getInstance();
		if (ServerVersion.V1_12.isEqualOrOlder()) {
			if (!vanishHook.isEnabled()) {
				pluginManager.registerEvents(new VanishListener1_12(), this);
			}
			pluginManager.registerEvents(new InventoryListener1_12(), this);
		} else {
			if (!vanishHook.isEnabled()) {
				pluginManager.registerEvents(new VanishListener1_11(), this);
			}
			pluginManager.registerEvents(new InventoryListener1_11(), this);
		}
		new Metrics(this);
		this.sendMessage("§2" + this.getDescription().getName() + "§a by Tristiisch (" + this.getDescription().getVersion() + ") is activated.");
	}
}
