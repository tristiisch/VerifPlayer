package fr.tristiisch.verifplayer.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotUpdater {

	private int project = 0;
	private URL checkURL;
	private String latestVersion;
	private final JavaPlugin plugin;

	public SpigotUpdater(final JavaPlugin plugin, final int projectID) {
		this.plugin = plugin;
		this.project = projectID;
		try {
			this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
		} catch(final MalformedURLException e) {
			e.printStackTrace();
		}
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> this.checkForUpdates());
	}

	public void checkForUpdates() {
		try {
			if(this.needUpdate()) {
				final ConsoleCommandSender console = this.plugin.getServer().getConsoleSender();
				console.sendMessage("§4[§c" + this.plugin.getDescription().getName() + "§4] §6Your version (" + this.plugin.getDescription().getVersion() + ") is deprecated, download version " + this
						.getLatestVersion() + " here: " + this.getResourceURL());
			}
		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public String getLatestVersion() {
		return this.latestVersion;
	}

	public JavaPlugin getPlugin() {
		return this.plugin;
	}

	public int getProjectID() {
		return this.project;
	}

	public String getResourceURL() {
		return "https://www.spigotmc.org/resources/" + this.project;
	}

	public boolean needUpdate() throws Exception {
		final URLConnection con = this.checkURL.openConnection();
		this.latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		final float pluginVersion = Float.parseFloat(this.plugin.getDescription().getVersion());
		final float lastVersion = Float.parseFloat(this.latestVersion);
		return pluginVersion < lastVersion;
	}

}