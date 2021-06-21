package fr.tristiisch.verifplayer.playerinfo;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.hook.HookHandler;

public abstract class PlayerInfoHook {

	private Player player;

	public PlayerInfoHook(Player player) {
		this.player = player;
	}

	public String getName() {
		return player.getName();
	}

	public Player getPlayer() {
		return player;
	}

	public String getPrefix() {
		return getRank() + getName();
	}

	public String getRank() {
		return HookHandler.getInstance().getGroupPrefix(player);
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}
}
