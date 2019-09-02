package fr.tristiisch.verifplayer.utils.config;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.config.CustomConfigs.CustomConfig;

public enum ConfigGet {

	MESSAGES_ALERTCPS_ALREADYDISABLED,
	MESSAGES_ALERTCPS_ALREADYENABLED,
	MESSAGES_ALERTCPS_DISABLED,
	MESSAGES_ALERTCPS_ENABLED,
	MESSAGES_ALERTCPS_USAGE,
	MESSAGES_ALERTCPS_NOPERMTORECEIVEALERTS,
	MESSAGES_CANTCONSOLE,
	MESSAGES_NOPERM,
	MESSAGES_VERIF_ISNOTCONNECTED,
	MESSAGES_VERIF_PLAYERDISCONNECT,
	MESSAGES_VERIF_SENDALERT,
	MESSAGES_VERIF_CANTVERIFYOURSELF,
	MESSAGES_VERIF_USAGE,
	MESSAGES_VERIFGUI_CPSFORMAT,
	MESSAGES_VERIFGUI_CPSALERTFORMAT,
	MESSAGES_VERIFGUI_CPSITEMNAME,
	MESSAGES_VERIFGUI_CPSMAX,
	MESSAGES_VERIFGUI_EFFECT,
	MESSAGES_VERIFGUI_INFO,
	MESSAGES_VERIFGUI_NAME,
	MESSAGES_VERIFGUI_NBALERT,
	MESSAGES_VERIFGUI_NBALERTS,
	MESSAGES_VERIFGUI_NOEFFECT,
	MESSAGES_VERIFGUI_NOPARTICLE,
	MESSAGES_VERIFGUI_PINGFORMAT,
	MESSAGES_VERIFGUI_PINGITEMNAME,
	MESSAGES_VERIFGUI_TPSFORMAT,
	MESSAGES_VERIFGUI_TPSITEMNAME,
	MESSAGES_VERIF_CANTVERIFADMIN,
	MESSAGES_TIME_YEAR,
	MESSAGES_TIME_YEARS,
	MESSAGES_TIME_MONTH,
	MESSAGES_TIME_MONTHS,
	MESSAGES_TIME_DAY,
	MESSAGES_TIME_DAYS,
	MESSAGES_TIME_HOUR,
	MESSAGES_TIME_HOURS,
	MESSAGES_TIME_MINUTE,
	MESSAGES_TIME_MINUTES,
	MESSAGES_TIME_SECOND,
	MESSAGES_TIME_SECONDS,

	VERSION,
	SETTINGS_SIZEHISTORYCPS,
	SETTINGS_MAXCPS,
	SETTINGS_TIMEBETWEENALERTS,
	SETTINGS_LANGUAGE,

	MESSAGES_FREEZE_TITLE,
	MESSAGES_FREEZE_SUBTITLE,
	MESSAGES_FREEZE_SUBTITLEREASON,
	MESSAGES_FREEZE_PLAYERNOTCONNECTED,
	MESSAGES_FREEZE_PLAYERUNFREEZE,
	MESSAGES_FREEZE_PLAYERFREEZE,
	MESSAGES_FREEZE_PLAYERFREEZEREASON,
	MESSAGES_FREEZE_USAGE,
	MESSAGES_FREEZE_NOLONGERFREEZETITLE,
	MESSAGES_FREEZE_NOLONGERFREEZESUBTITLE,
	MESSAGES_FREEZE_PLAYERISFREEZE,
	MESSAGES_FREEZE_PLAYERDISCONNECTWHILEFREEZE,
	MESSAGES_VANISH_ENABLE,
	MESSAGES_VANISH_DISABLE,
	MESSAGES_VERIFSPEC_TELEPORTERMSG_NOPLAYERS,
	MESSAGES_VERIFSPEC_TELEPORTERMSG_SUCCES,
	MESSAGES_VERIFSPEC_DISABLE,
	MESSAGES_VERIFSPEC_ENABLE,
	MESSAGES_VERIFSPEC_SHOULDBEONGROUND,
	MESSAGES_VERIFSPEC_DISTANCETOOFAR,
	MESSAGES_VERIFSPEC_TOOLSPEED,
	MESSAGES_VERIFSPEC_TOOLSPEEDNORMAL,;

	CustomConfig customConfig;

	static {
		for (final ConfigGet configGet : ConfigGet.values()) {

			if (configGet.toString().startsWith("MESSAGES")) {
				configGet.setCustomConfig(CustomConfig.MESSAGES);
			} else {
				configGet.setCustomConfig(CustomConfig.DEFAULT);
			}

			final YamlConfiguration config = configGet.getConfig();

			if (config.get(configGet.getPath(), null) == null) {
				VerifPlayerPlugin.getInstance().sendMessage("The value in config '" + configGet.getCustomConfig().getFileName() + "' '" + configGet.getPath() + "' is null.");
			}
		}
	}

	private void setCustomConfig(CustomConfig customConfig) {
		this.customConfig = customConfig;
	}

	public CustomConfig getCustomConfig() {
		// if(this.toString().startsWith("MESSAGES")) {
		// customConfig = CustomConfig.MESSAGES;
		// return CustomConfig.MESSAGES;
		// }
		// customConfig = CustomConfig.DEFAULT;
		// return CustomConfig.DEFAULT;
		return this.customConfig;
	}

	public YamlConfiguration getConfig() {
		return this.getCustomConfig().getConfig();
	}

	public double getDouble() {
		return this.getConfig().getDouble(this.getPath());
	}

	public int getInt() {
		return this.getConfig().getInt(this.getPath());
	}

	private String getPath() {
		return this.name().toLowerCase().toString().replaceAll("_", ".");
	}

	public String getString() {
		return SpigotUtils.color(this.getStringBrut());
	}

	public String getStringBrut() {
		return this.getConfig().getString(this.getPath());
	}

	public List<String> getStringList() {
		return SpigotUtils.color(this.getConfig().getStringList(this.getPath()));
	}
}
