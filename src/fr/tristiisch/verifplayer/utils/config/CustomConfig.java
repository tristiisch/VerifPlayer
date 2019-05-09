package fr.tristiisch.verifplayer.utils.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

public class CustomConfig {

	public enum CustomConfigs {
		DEFAULT("config.yml"),
		MESSAGES("messages-%lang%.yml");

		private final String fileName;
		private YamlConfiguration config;

		private CustomConfigs(final String fileName) {
			this.fileName = fileName;
		}

		private void createFolderIfNotExist() {
			if(!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdir();
			}
		}

		public YamlConfiguration getConfig() {
			return this.config;
		}

		public File getFile() {
			return new File(plugin.getDataFolder(), this.getFileName());
		}

		public File getFile(final String fileName) {
			return new File(plugin.getDataFolder(), fileName);
		}

		public String getFileName() {
			if(this.toString().equals("MESSAGES")) {
				return this.fileName.replace("%lang%", langague);
			}
			return this.fileName;
		}

		public void load() {
			this.createFolderIfNotExist();
			final String fileName = this.getFileName();
			try {
				final File configFile = this.getFile(fileName);
				if(!configFile.exists()) {
					configFile.createNewFile();
					if(plugin.getResource(fileName) != null) {
						ByteStreams.copy(plugin.getResource(fileName), new FileOutputStream(configFile));
					} else {
						Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Config \"" + fileName + "\" unknown.");
					}
				}
				this.config = YamlConfiguration.loadConfiguration(configFile);
				if(this.toString().equals("DEFAULT")) {
					langague = this.config.getString("settings.language");
				}
			} catch(final IOException e) {
				Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Unable to load config: " + fileName);
				e.printStackTrace();
			}
		}

		public void save() {
			final String fileName = this.getFileName();
			final File configFile = this.getFile(fileName);
			try {
				this.config.save(configFile);
			} catch(final IOException e) {
				Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Unable to save config: " + fileName);
				e.printStackTrace();
			}
		}
	}

	public static String langague = "en";
	private static Plugin plugin;

	public static double loadConfigs(final Plugin plugin) {
		CustomConfig.plugin = plugin;
		return reloadConfigs();
	}

	public static double reloadConfigs() {
		final long time = System.nanoTime();
		for(final CustomConfigs config : CustomConfigs.values()) {
			config.load();
		}
		return (double) System.nanoTime() - time;
	}

	public static double saveConfigs() {
		final long time = System.nanoTime();
		for(final CustomConfigs config : CustomConfigs.values()) {
			config.save();
		}
		return (double) System.nanoTime() - time;

	}
}
