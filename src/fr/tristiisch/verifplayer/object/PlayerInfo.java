package fr.tristiisch.verifplayer.object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInfo {

	private Integer clickAir = 0;
	private Integer clickEntity = 0;
	private final UUID uuid;
	// TODO use this instand of clicksAir & clicksEntity
	//private final LinkedHashMap<Integer, Integer> clicks = new LinkedHashMap<>();
	private final List<Integer> clicksAir = new ArrayList<>();
	private final List<Integer> clicksEntity = new ArrayList<>();
	private long lastAlert = 0L;
	/*	public long lastBlockInteraction = 0L;
		public long lastBlockPlace = 0L;*/

	private int maxCPS = 0;

	private int nbAlerts = 0;

	public PlayerInfo(final Player player) {
		this.uuid = player.getUniqueId();
	}

	public void addClickAir() {
		if(this.clickAir > this.maxCPS) {
			this.maxCPS = this.clickAir;
		}
		this.clickAir++;
	}

	public void addClickEntity() {
		this.clickEntity++;
	}

	public void addNbAlerts() {
		this.nbAlerts++;
	}

	public Integer getClickAir() {
		return this.clickAir;
	}

	public Integer getClickEntity() {
		return this.clickEntity;
	}

	public List<Integer> getClicksAir() {
		return this.clicksAir;
	}

	public List<Integer> getClicksEntity() {
		return this.clicksEntity;
	}

	public int getCurrentClicks() {
		final int i = this.clicksAir.size();
		if(i == 0) {
			return 0;
		}
		return this.clicksAir.get(i - 1) + this.clicksEntity.get(i - 1);
	}

	public long getLastAlert() {
		return this.lastAlert;
	}

	public int getMaxCPS() {
		return this.maxCPS;
	}

	public int getNbAlerts() {
		return this.nbAlerts;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public UUID getUniqueId() {
		return this.uuid;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void removeClickAir() {
		this.clickAir--;
	}

	public void resetClickAir() {
		this.clickAir = 0;
	}

	public void resetClickEntity() {
		this.clickEntity = 0;
	}

	public void setLastAlert(final long timestamp) {
		this.lastAlert = timestamp;
	}
}
