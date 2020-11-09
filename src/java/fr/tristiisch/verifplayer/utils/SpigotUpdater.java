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
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> checkForUpdates());
	}

	public void checkForUpdates() {
		try {
			String pluginVersion = plugin.getDescription().getVersion();
			if (checkNeedUpdate()) {

				if (!compatibleServerVersion)
					plugin.sendMessage("§cYour version (" + pluginVersion + ") is NOT compatible with the server version, download good version (last is " + getLatestVersion() + ", your is " + plugin.getDescription().getVersion()
							+ ") here: " + getResourceURL());
				else
					plugin.sendMessage("§6Your version (" + pluginVersion + ") is deprecated, download last version " + getLatestVersion() + " here: " + getResourceURL());

			} else
				plugin.sendMessage("§aYour version is up to date");
		} catch (UnknownHostException e) {
			plugin.sendMessage("§6You are not connected to internet, update messages are ignored");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkNeedUpdate() throws IOException {
		URLConnection con = getCheckURL().openConnection();
		latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		float pluginVersion = Float.parseFloat(plugin.getDescription().getVersion());
		float lastVersion = Float.parseFloat(latestVersion);
		needUpdate = pluginVersion < lastVersion;
		return needUpdate;
	}

	public String getActualVersion() {
		return plugin.getDescription().getVersion();
	}

	private URL getCheckURL() throws MalformedURLException {
		return new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	private String getResourceURL() {
		return "https://www.spigotmc.org/resources/" + projectId;
	}

	public boolean isCompatibleServerVersion() {
		return compatibleServerVersion;
	}

	public boolean isNeededUpdate() {
		return needUpdate;
	}

	public void setCompatibleServerVersion(boolean compatibleServerVersion) {
		this.compatibleServerVersion = compatibleServerVersion;
	}

}