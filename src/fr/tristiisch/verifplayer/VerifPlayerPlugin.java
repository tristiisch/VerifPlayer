package fr.tristiisch.verifplayer;

import org.bukkit.plugin.java.JavaPlugin;

import fr.tristiisch.verifplayer.core.vanish.VanishHandler;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiHandler;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfoHandler;
import fr.tristiisch.verifplayer.task.TaskHandler;
import fr.tristiisch.verifplayer.tps.TpsGetter;
import fr.tristiisch.verifplayer.utils.SpigotUtils;

public abstract class VerifPlayerPlugin extends JavaPlugin {

	protected static VerifPlayerPlugin instance;

	public static VerifPlayerPlugin getInstance() {
		return instance;
	}

	private final PlayerInfoHandler playerInfoHandler = new PlayerInfoHandler();
	private final TaskHandler taskHandler = new TaskHandler(this);
	private final VerifGuiHandler verifGuiHandler = new VerifGuiHandler();
	private final VanishHandler vanishHandler = new VanishHandler();

	public VerifPlayerPlugin() {
		instance = this;
	}

	public PlayerInfoHandler getPlayerInfoHandler() {
		return this.playerInfoHandler;
	}

	public String getPrefix() {
		return "&b[&3" + this.getDescription().getName() + "&b] &c";
	}

	public TaskHandler getTaskHandler() {
		return this.taskHandler;
	}

	public TpsGetter getTpsGetter() {
		return new TpsGetter();
	}

	public VanishHandler getVanishHandler() {
		return this.vanishHandler;
	}

	public VerifGuiHandler getVerifGuiHandler() {
		return this.verifGuiHandler;
	}

	public void sendMessage(final String message) {
		this.getServer().getConsoleSender().sendMessage(SpigotUtils.color(this.getPrefix() + message));
	}
}
