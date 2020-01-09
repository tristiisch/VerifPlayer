package fr.tristiisch.verifplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import fr.tristiisch.verifplayer.VerifPlayer;

public class SpigotUpdater {

	private static SpigotUpdater INSTANCE;

	public static SpigotUpdater getInstance() {
		return INSTANCE;
	}

	private VerifPlayer plugin;
	private int projectId;
	private String latestVersion;
	private boolean compatibleServerVersion = true;
	private boolean needUpdate;

	public SpigotUpdater(VerifPlayer plugin, int projectId) {
		SpigotUpdater.INSTANCE = this;
		this.plugin = plugin;
		this.projectId = projectId;
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> this.checkForUpdates());
	}

	public void checkForUpdates() {
		try {
			String pluginVersion = this.plugin.getDescription().getVersion();
			if (this.checkNeedUpdate()) {

				if (!this.compatibleServerVersion) {
					this.plugin.sendMessage("§cYour version (" + pluginVersion + ") is NOT compatible with the server version, download last version " + this.getLatestVersion() + " here: " + this.getResourceURL());
				} else {
					this.plugin.sendMessage("§6Your version (" + pluginVersion + ") is deprecated, download last version " + this.getLatestVersion() + " here: " + this.getResourceURL());
				}

			} else {
				this.plugin.sendMessage("§aYour version is up to date");
			}
		} catch (UnknownHostException e) {
			this.plugin.sendMessage("§6You are not connected to internet, update messages are ignored");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkNeedUpdate() throws IOException {
		URLConnection con = this.getCheckURL().openConnection();
		this.latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		float pluginVersion = Float.parseFloat(this.plugin.getDescription().getVersion());
		float lastVersion = Float.parseFloat(this.latestVersion);
		this.needUpdate = pluginVersion < lastVersion;
		return this.needUpdate;
	}

	public String getActualVersion() {
		return this.plugin.getDescription().getVersion();
	}

	private URL getCheckURL() throws MalformedURLException {
		return new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.projectId);
	}

	public String getLatestVersion() {
		return this.latestVersion;
	}

	private String getResourceURL() {
		return "https://www.spigotmc.org/resources/" + this.projectId;
	}

	public boolean isCompatibleServerVersion() {
		return this.compatibleServerVersion;
	}

	public boolean isNeededUpdate() {
		return this.needUpdate;
	}

	public void setCompatibleServerVersion(boolean compatibleServerVersion) {
		this.compatibleServerVersion = compatibleServerVersion;
	}

}