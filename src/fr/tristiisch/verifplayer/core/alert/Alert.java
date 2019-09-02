package fr.tristiisch.verifplayer.core.alert;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Alert {

	private static final Set<UUID> alertsDisabled = new HashSet<>();

	public static boolean alertsEnabled(final Player player) {
		return alertsDisabled.contains(player.getUniqueId()) ? false : true;
	}

	public static boolean disableAlerts(Player player) {
		return alertsDisabled.add(player.getUniqueId());
	}

	public static boolean enableAlerts(Player player) {
		return alertsDisabled.remove(player.getUniqueId());
	}
}
