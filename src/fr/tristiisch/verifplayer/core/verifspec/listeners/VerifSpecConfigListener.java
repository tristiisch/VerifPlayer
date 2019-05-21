package fr.tristiisch.verifplayer.core.verifspec.listeners;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.tristiisch.verifplayer.utils.config.CustomConfig.CustomConfigs;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.utils.config.CustomConfigLoadEvent;
import fr.tristiisch.verifplayer.utils.item.ItemCreator;

public class VerifSpecConfigListener implements Listener {

	@EventHandler
	public void onCustomConfigLoad(final CustomConfigLoadEvent event) {
		final CustomConfigs customConfig = event.getCustomConfig();
		if(!customConfig.isSameConfig(CustomConfigs.MESSAGES)) {
			return;
		}

		final YamlConfiguration config = customConfig.getConfig();
		for(final VerifSpecTool verifSpecItems : VerifSpecTool.values()) {
			final ItemCreator itemCreator = verifSpecItems.getItemCreator();
			final ConfigurationSection configSection = config.getConfigurationSection("messages.verifspec." + verifSpecItems.getName());

			final String materialName = configSection.getString("item");
			final Material material = Material.getMaterial(materialName);
			if(material == null) {
				Bukkit.getLogger().log(Level.SEVERE, "§cMaterial '" + materialName + "' not found. See https://helpch.at/docs/1.12.2/index.html?org/bukkit/class-use/Material.html");
				continue;
			}
			itemCreator.material(material);

			final String customName = configSection.getString("name");
			itemCreator.customName(customName);

			final List<String> lore = configSection.getStringList("lore");
			itemCreator.lore(lore);

			final int slot = configSection.getInt("slot");
			verifSpecItems.setSlot(slot);

			final boolean enable = configSection.getBoolean("enable");
			verifSpecItems.setEnable(enable);

			VerifSpecTool.getTool(verifSpecItems.getName()).setItemCreator(itemCreator);
		}
	}

}
