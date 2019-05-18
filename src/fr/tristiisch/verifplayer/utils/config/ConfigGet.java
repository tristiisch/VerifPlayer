package fr.tristiisch.verifplayer.utils.config;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.CustomConfig.CustomConfigs;

public enum ConfigGet {

	MESSAGES_VERSION,
	MESSAGES_ALERTCPS_ALREADY_DISABLED,
	MESSAGES_ALERTCPS_ALREADY_ENABLED,
	MESSAGES_ALERTCPS_DISABLED,
	MESSAGES_ALERTCPS_ENABLED,
	MESSAGES_ALERTCPS_USAGE,
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
	;

	public YamlConfiguration getConfig() {
		if(this.toString().startsWith("MESSAGES")) {
			return CustomConfigs.MESSAGES.getConfig();
		}
		return CustomConfigs.DEFAULT.getConfig();
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
		return Utils.color(this.getStringBrut());
	}

	public String getStringBrut() {
		return this.getConfig().getString(this.getPath());
	}

	public List<String> getStringList() {
		return Utils.color(this.getConfig().getStringList(this.getPath()));
	}
}
