package fr.tristiisch.verifplayer.utils.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.ByteStreams;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class CustomConfigs {

	public enum CustomConfig {
		DEFAULT("config.yml"),
		MESSAGES("messages-%lang%.yml"),
		INVENTORY("inventory.yml");

		private String fileName;
		private YamlConfiguration config;

		private CustomConfig(String fileName) {
			this.fileName = fileName;
		}

		private void createFolderIfNotExist() {
			File folder = this.getFolder();
			if (!folder.exists()) {
				folder.mkdir();
			}
		}

		public YamlConfiguration getConfig() {
			return this.config;
		}

		public File getFile() {
			return this.getFile(this.getFileName());
		}

		public File getFile(String fileName) {
			return new File(this.getFolder(), fileName);
		}

		public String getFileName() {
			if (this.equals(CustomConfig.MESSAGES)) {
				return this.fileName.replace("%lang%", langague);
			}
			return this.fileName;
		}

		private File getFolder() {
			return plugin.getDataFolder();
		}

		private InputStream getResource(String fileName) {
			return plugin.getResource(fileName);
		}

		public boolean isSameConfig(CustomConfig customConfig) {
			return this.toString().equals(customConfig.toString());
		}

		public void load() {
			this.createFolderIfNotExist();
			String fileName = this.getFileName();
			try {
				File configFile = this.getFile(fileName);
				InputStream resource = this.getResource(fileName);
				if (!configFile.exists()) {
					configFile.createNewFile();
					if (resource != null) {
						ByteStreams.copy(resource, new FileOutputStream(configFile));
					}
				} else if (resource != null) {

					YamlConfiguration configInFolder = YamlConfiguration.loadConfiguration(configFile);
					YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resource));
					if (jarConfig.getDouble("version") > configInFolder.getDouble("version")) {
						String newName = fileName + " V" + configInFolder.getDouble("version");
						plugin.sendMessage("Your file '" + fileName + "' has been updated, I use now DEFAULT CONFIG. Your old configuration is now called '" + newName + ".");
						configFile.renameTo(new File(this.getFolder(), newName));
						configFile = new File(this.getFolder(), fileName);
						configFile.createNewFile();
						ByteStreams.copy(this.getResource(fileName), new FileOutputStream(configFile));
					}
				}

				this.config = YamlConfiguration.loadConfiguration(configFile);
				if (this.equals(CustomConfig.DEFAULT)) {
					String lang = this.config.getString("settings.language");
					if (lang != null && lang.isEmpty()) {
						plugin.sendMessage("Language in config.yml where empty, so we load English messages. ");
					} else {
						langague = lang;
					}
				}
				plugin.getServer().getPluginManager().callEvent(new CustomConfigLoadEvent(this));
			} catch (IOException e) {
				plugin.sendMessage("Unable to load config: " + fileName);
				e.printStackTrace();
			}
		}

		public void save() {
			String fileName = this.getFileName();
			File configFile = this.getFile(fileName);
			try {
				this.config.save(configFile);
			} catch (IOException e) {
				plugin.sendMessage("Unable to save config: " + fileName);
				e.printStackTrace();
			}
		}
	}

	private static String langague = "en";

	private static VerifPlayerPlugin plugin;

	public static String getLangague() {
		return langague;
	}

	public static double loadConfigs() {
		long time = System.nanoTime();
		for (CustomConfig config : CustomConfig.values()) {
			config.load();
		}
		return (System.nanoTime() - time) / 1000000000d;
	}

	public static double saveConfigs() {
		long time = System.nanoTime();
		for (CustomConfig config : CustomConfig.values()) {
			config.save();
		}
		return (System.nanoTime() - time) / 1000000000d;
	}

	public CustomConfigs(VerifPlayerPlugin plugin) {
		CustomConfigs.plugin = plugin;
		loadConfigs();
	}
}
