package fr.tristiisch.verifplayer;

import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.core.vanish.VanishHandler;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiHandler;
import fr.tristiisch.verifplayer.core.verifspec.PlayerListGuiHandler;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfoHandler;
import fr.tristiisch.verifplayer.task.TaskHandler;
import fr.tristiisch.verifplayer.tps.TpsGetter;
import fr.tristiisch.verifplayer.utils.SpigotUtils;

public abstract class VerifPlayerPlugin extends JavaPlugin {

	protected static VerifPlayerPlugin instance;

	public static VerifPlayerPlugin getInstance() {
		return instance;
	}

	private PlayerInfoHandler playerInfoHandler = new PlayerInfoHandler();
	private TaskHandler taskHandler = new TaskHandler(this);
	private VerifGuiHandler verifGuiHandler = new VerifGuiHandler();
	private VanishHandler vanishHandler = new VanishHandler();
	private PlayerListGuiHandler playerListGuiHandler = new PlayerListGuiHandler();

	public VerifPlayerPlugin() {
		instance = this;
	}

	public PlayerInfoHandler getPlayerInfoHandler() {
		return playerInfoHandler;
	}

	public String getPrefix() {
		return "&b[&3" + getDescription().getName() + "&b] &c";
	}

	public TaskHandler getTaskHandler() {
		return taskHandler;
	}

	public TpsGetter getTpsGetter() {
		return new TpsGetter();
	}

	public VanishHandler getVanishHandler() {
		return vanishHandler;
	}

	public VerifGuiHandler getVerifGuiHandler() {
		return verifGuiHandler;
	}

	public PlayerListGuiHandler getPlayerListGuiHandler() {
		return playerListGuiHandler;
	}

	public void sendMessage(String message) {
		getServer().getConsoleSender().sendMessage(SpigotUtils.color(getPrefix() + message));
	}
}
