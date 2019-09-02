package fr.tristiisch.verifplayer.playerinfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.core.verifclick.FastClickRunnable;

public class PlayerInfoHandler {

	/*
	 * private final Map<UUID, PlayerInfo> playersInfos = new HashMap<>();
	 * 
	 * public void addNew(final Player player) {
	 * this.playersInfos.put(player.getUniqueId(), new PlayerInfo());
	 * FastClickRunnable.startIfNotRunning(); }
	 * 
	 * public Map<UUID, PlayerInfo> get() { return this.playersInfos; }
	 * 
	 * public PlayerInfo get(final Player player) { return
	 * this.playersInfos.get(player.getUniqueId()); }
	 * 
	 * public PlayerInfo get(final UUID uuid) { return this.playersInfos.get(uuid);
	 * }
	 * 
	 * public void remove(final Player player) {
	 * this.playersInfos.remove(player.getUniqueId());
	 * if(this.playersInfos.isEmpty()) { FastClickRunnable.stop(); } }
	 * 
	 * public void set(final Player player, final PlayerInfo playerInfo) {
	 * this.playersInfos.put(player.getUniqueId(), playerInfo); }
	 */
	private final Set<PlayerInfo> playersInfos = new HashSet<>();

	public void addNew(final Player player) {
		this.playersInfos.add(new PlayerInfo(player));
		FastClickRunnable.startIfNotRunning();
	}

	public Set<PlayerInfo> get() {
		return this.playersInfos;
	}

	public PlayerInfo get(final Player player) {
		return this.get(player.getUniqueId());
	}

	public PlayerInfo get(final UUID uuid) {
		return this.playersInfos.stream().filter(playerInfo -> playerInfo.getUniqueId().equals(uuid)).findFirst().orElse(null);
	}

	public void remove(final Player player) {
		this.playersInfos.removeIf(playerInfo -> playerInfo.getUniqueId().equals(player.getUniqueId()));
		if (this.playersInfos.isEmpty()) {
			FastClickRunnable.stop();
		}
	}

	/*
	 * public void set(final Player player, final PlayerInfo playerInfo) {
	 * this.playersInfos.put(player.getUniqueId(), playerInfo); }
	 */
}
