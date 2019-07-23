package fr.tristiisch.verifplayer.utils.permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class PermissionManager {

	static String pluginName;

	public static void createPermFile(final Plugin plugin) {
		PermissionManager.pluginName = plugin.getName();
		final String configName = "permission";
		final String fileName = new StringBuilder(configName).append(".yml").toString();
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		final File configFile = new File(plugin.getDataFolder(), fileName);
		if(configFile.exists()) {
			configFile.delete();
		}
		try {
			configFile.createNewFile();
			final InputStream inputStream = plugin.getResource(fileName);
			if(inputStream != null) {
				ByteStreams.copy(inputStream, new FileOutputStream(configFile));
			}

		} catch(final Exception e) {
			VerifPlayerPlugin.getInstance().sendMessage("Unable to create file " + fileName);
			e.printStackTrace();
		}

		final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		for(final Permission perm : Permission.values()) {
			final ConfigurationSection section = config.createSection("permissions." + perm.getId().replaceAll("//.", "	\\u002E"));

			section.set("description", perm.getDescription());
			section.set("default", "op");
		}
		try {
			config.save(configFile);
		} catch(final IOException e) {
			VerifPlayerPlugin.getInstance().sendMessage("Unable to save file " + fileName);
			e.printStackTrace();
		}
	}
}
