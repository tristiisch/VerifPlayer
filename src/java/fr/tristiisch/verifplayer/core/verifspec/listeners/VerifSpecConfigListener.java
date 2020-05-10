package fr.tristiisch.verifplayer.core.verifspec.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.utils.config.CustomConfigLoadEvent;
import fr.tristiisch.verifplayer.utils.config.CustomConfigs.CustomConfig;
import fr.tristiisch.verifplayer.utils.item.ItemCreator;

public class VerifSpecConfigListener implements Listener {

	@EventHandler
	public void onCustomConfigLoad(CustomConfigLoadEvent event) {
		CustomConfig customConfig = event.getCustomConfig();
		if (!customConfig.isSameConfig(CustomConfig.MESSAGES)) {
			return;
		}

		YamlConfiguration config = customConfig.getConfig();
		for (VerifSpecTool verifSpecItems : VerifSpecTool.values()) {
			ItemCreator itemCreator = verifSpecItems.getItemCreator();
			ConfigurationSection configSection = config.getConfigurationSection("messages.verifspec." + verifSpecItems.getName());

			if (configSection == null) {
				VerifPlayerPlugin.getInstance().sendMessage("Tool '" + verifSpecItems.getName() + "' dosen't exist. You have to remove it from messages.yml");
				continue;
			}

			String materialName = configSection.getString("item");
			Material material = null;

			if (materialName != null) {
				material = Material.getMaterial(materialName);
			}

			if (material == null) {
				VerifPlayerPlugin.getInstance().sendMessage("Material '" + materialName + "' not found. See https://helpch.at/docs/1.12.2/index.html?org/bukkit/class-use/Material.html");
				continue;
			}
			itemCreator.material(material);

			String customName = configSection.getString("name");
			itemCreator.customName(customName);

			List<String> lore = configSection.getStringList("lore");
			itemCreator.lore(lore);

			boolean attribute = configSection.getBoolean("attributehide");
			if (attribute) {
				itemCreator.attributeHide();
			}

			int slot = configSection.getInt("slot");
			verifSpecItems.setSlot(slot);

			boolean enable = configSection.getBoolean("enable");
			verifSpecItems.setEnable(enable);

			VerifSpecTool.getTool(verifSpecItems.getName()).setItemCreator(itemCreator);
		}
	}
}
