package fr.tristiisch.verifplayer.core.vanish;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import fr.tristiisch.verifplayer.Main;
import fr.tristiisch.verifplayer.hook.EssentialsHook;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class Vanish {

	private static List<Player> vanish = new ArrayList<>();

	public static void addVanish(final Player player) {
		vanish.add(player);
	}

	@SuppressWarnings("deprecation")
	public static void disable(final Player player, final boolean showMessage) {
		Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
		
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.spigot().setCollidesWithEntities(true);

		final Essentials ess = EssentialsHook.getInstance().getEssentials();
		if(ess != null) {
			final User user = ess.getUser(player);
			user.setVanished(false);
		}
		player.removeMetadata("vanished", Main.getInstance());
		removeVanish(player);
		if(showMessage) {
			player.sendMessage(ConfigGet.MESSAGES_VANISH_DISABLE.getString());
			// player.sendMessage(SpigotUtils.color("&cMode invisible désactivé."));
		}
	}

	@SuppressWarnings("deprecation")
	public static void enable(final Player player, final boolean showMessage) {
		Permission.ADMIN_SEEVANISHED.getOnlinePlayersOpositeStream().forEach(noAdmin -> noAdmin.hidePlayer(player));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false), true);
		player.spigot().setCollidesWithEntities(false);
		/* GhostUtils.ghostFactory.addPlayer(player); */
		final Essentials ess = EssentialsHook.getInstance().getEssentials();
		if(ess != null) {
			final User user = ess.getUser(player);
			user.setVanished(true);
		}
		player.setMetadata("vanished", new FixedMetadataValue(Main.getInstance(), true));
		addVanish(player);
		if(showMessage) {
			ConfigGet.MESSAGES_VANISH_ENABLE.getString();
			player.sendMessage(ConfigGet.MESSAGES_VANISH_ENABLE.getString());
			// player.sendMessage(SpigotUtils.color("&aMode invisible activé."));
		}
	}

	public static Stream<? extends Player> getVanished() {
		return Bukkit.getOnlinePlayers().stream().filter(players -> isVanished(players));
	}

	public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
}
	
	public static void removeVanish(final Player player) {
		vanish.remove(player);
	}
}
