package fr.tristiisch.verifplayer.playerinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInfo extends PlayerInfoHook {

	private int clickAir = 0;
	private int clickEntity = 0;
	private int maxCPS = 0;
	private int alert = 0;
	private long lastAlert = 0L;

	/**
	 * private boolean freezed; private boolean alertEnabled; private boolean
	 * verifSpecEnabled;
	 **/

	private final List<Integer> clicksAir = new ArrayList<>();

	private final List<Integer> clicksEntity = new ArrayList<>();
	private final Map<Long, Integer> alertHistory = new HashMap<>();
	private List<UUID> alreadyTeleportedPlayers = new ArrayList<>();

	public PlayerInfo(final Player player) {
		super(player);
	}

	public void addAirClick() {
		++this.clickAir;
	}

	public void addAlert() {
		++this.alert;
	}

	public void addAlertHistory(final long time, final int value) {
		this.alertHistory.put(time, value);
	}

	public void addEntityClick() {
		++this.clickEntity;
	}

	public void clearAlreadyTeleportedPlayers() {
		this.alreadyTeleportedPlayers.clear();
	}

	public List<Integer> getAirClicks() {
		return this.clicksAir;
	}

	public Map<Long, Integer> getAlertHistory() {
		return this.alertHistory;
	}

	public int getNumberAlert() {
		return this.alert;
	}

	public List<Player> getAlreadyTeleportedPlayers() {
		return this.alreadyTeleportedPlayers.stream().map(uuid -> Bukkit.getPlayer(uuid)).filter(player -> player != null).collect(Collectors.toList());
	}

	public int getClickAir() {
		return this.clickAir;
	}

	public int getClickEntity() {
		return this.clickEntity;
	}

	public int getCurrentClicks() {
		final int i = this.clicksAir.size();
		if (i == 0) {
			return 0;
		}
		return this.clicksAir.get(i - 1) + this.clicksEntity.get(i - 1);
	}

	public List<Integer> getEntityClicks() {
		return this.clicksEntity;
	}

	public long getLastAlert() {
		return this.lastAlert;
	}

	public int getMaxCPS() {
		return this.maxCPS;
	}

	public void removeAirClick() {
		--this.clickAir;
	}

	public void resetAirClick() {
		this.clickAir = 0;
	}

	public void resetEntityClick() {
		this.clickEntity = 0;
	}

	public void setAlreadyTeleportedPlayers(final List<Player> alreadyTeleportedPlayers) {
		this.alreadyTeleportedPlayers = alreadyTeleportedPlayers.stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	public void setLastAlert(final long lastAlert) {
		this.lastAlert = lastAlert;
	}

	public void setMaxCPS(final int maxCPS) {
		this.maxCPS = maxCPS;
	}
}
