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
		return this.player.getName();
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getRank() {
		return HookHandler.getInstance().getGroup(this.player);
	}

	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
}
