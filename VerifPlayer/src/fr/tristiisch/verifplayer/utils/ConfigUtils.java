package fr.tristiisch.verifplayer.utils;

import java.util.List;

import fr.tristiisch.verifplayer.Main;

public enum ConfigUtils {

	ALERTCPS_ALREADY_DISABLED,
	ALERTCPS_ALREADY_ENABLED,
	ALERTCPS_DISABLED,
	ALERTCPS_ENABLED,
	ALERTCPS_USAGE,
	CANTCONSOLE,
	NOPERM,
	VERIF_ISNOTCONNECTED,
	VERIF_PLAYERDISCONNECT,
	VERIF_SENDALERT,
	VERIF_USAGE,
	VERIFGUI_CPSFORMAT,
	VERIFGUI_CPSITEMNAME,
	VERIFGUI_CPSMAX,
	VERIFGUI_EFFECT,
	VERIFGUI_INFO,
	VERIFGUI_NAME,
	VERIFGUI_NBALERT,
	VERIFGUI_NOEFFECT,
	VERIFGUI_NOPARTICLE,
	VERIFGUI_PINGFORMAT,
	VERIFGUI_PINGITEMNAME,
	VERIFGUI_TPSFORMAT,
	VERIFGUI_TPSITEMNAME;

	public String getString() {
		return Utils.color(Main.getInstance().getConfig().getString("messages." + this.name().toLowerCase().toString()));
	}

	public List<String> getStringList() {
		return Utils.color(Main.getInstance().getConfig().getStringList("messages." + this.name().toLowerCase().toString()));
	}
}
