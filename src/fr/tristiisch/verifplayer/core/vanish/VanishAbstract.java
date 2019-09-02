package fr.tristiisch.verifplayer.core.vanish;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class VanishAbstract {

	public void addVanishMetadata(final Player player) {
		player.setMetadata("vanished", new FixedMetadataValue(VerifPlayerPlugin.getInstance(), true));
	}

	public Stream<? extends Player> getVanished() {
		return Bukkit.getOnlinePlayers().stream().filter(players -> this.isVanished(players));
	}

	public boolean isVanished(final Player player) {
		for (final MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) {
				return true;
			}
		}
		return false;
	}

	public void removeVanishMetadata(final Player player) {
		player.removeMetadata("vanished", VerifPlayerPlugin.getInstance());
	}
}
