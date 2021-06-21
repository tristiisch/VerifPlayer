package fr.tristiisch.verifplayer;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import fr.tristiisch.verifplayer.core.alert.AlertCpsCommand;
import fr.tristiisch.verifplayer.core.alert.AlertCpsListener;
import fr.tristiisch.verifplayer.core.command.VerifCommand;
import fr.tristiisch.verifplayer.core.command.VerifPlayerCommand;
import fr.tristiisch.verifplayer.core.freeze.FreezeCommand;
import fr.tristiisch.verifplayer.core.freeze.FreezeListener;
import fr.tristiisch.verifplayer.core.freeze.FreezeListener1_11;
import fr.tristiisch.verifplayer.core.freeze.FreezeListener1_12;
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
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecCommand;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecConfigListener;
import fr.tristiisch.verifplayer.core.verifspec.listeners.VerifSpecGuiListener;
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

		for (Player p : VerifSpec.getPlayersInVerif().stream().map(uuid -> Bukkit.getPlayer(uuid)).filter(p -> p != null && p.isOnline()).collect(Collectors.toList()))
			VerifSpec.disable(p);
		getVerifGuiHandler().closeAll();
		sendMessage("§4" + getDescription().getName() + "§c by Tristiisch (" + getDescription().getVersion() + ") is disabled.");
	}

	@Override
	public void onEnable() {
		getVerifGuiHandler().closeAll();
		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new VerifSpecConfigListener(), this);

		new CustomConfigs(this);
		new SpigotUpdater(this, 67212);
		new VersionUtils(this);
		new HookHandler(this);

		getCommand("verifplayer").setExecutor(new VerifPlayerCommand());
		getCommand("verif").setExecutor(new VerifCommand());
		getCommand("alertcps").setExecutor(new AlertCpsCommand());

		getCommand("verifspec").setExecutor(new VerifSpecCommand());
		getCommand("freeze").setExecutor(new FreezeCommand());
		getCommand("verifvanish").setExecutor(new VanishCommand());

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
		pluginManager.registerEvents(new VerifSpecGuiListener(), this);

		VanishHook vanishHook = VanishHook.getInstance();
		if (ServerVersion.V1_12.isEqualOrOlder()) {
			pluginManager.registerEvents(new FreezeListener1_12(), this);
			if (!vanishHook.isEnabled())
				pluginManager.registerEvents(new VanishListener1_12(), this);
			pluginManager.registerEvents(new InventoryListener1_12(), this);
		} else {
			pluginManager.registerEvents(new FreezeListener1_11(), this);
			if (!vanishHook.isEnabled())
				pluginManager.registerEvents(new VanishListener1_11(), this);
			pluginManager.registerEvents(new InventoryListener1_11(), this);
		}
		new Metrics(this);
		sendMessage("§2" + getDescription().getName() + "§a by Tristiisch (" + getDescription().getVersion() + ") is activated.");
	}
}
