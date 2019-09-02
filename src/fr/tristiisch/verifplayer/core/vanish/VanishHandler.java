package fr.tristiisch.verifplayer.core.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import fr.tristiisch.verifplayer.hook.EssentialsHook;
import fr.tristiisch.verifplayer.hook.VanishHook;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VanishHandler extends VanishAbstract {

	@SuppressWarnings("deprecation")
	public void disable(final Player player, final boolean showMessage) {

		final VanishHook vanishHook = VanishHook.getInstance();
		if (!vanishHook.isEnabled()) {
			Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.spigot().setCollidesWithEntities(true);
			removeVanishMetadata(player);

			final Essentials ess = EssentialsHook.getInstance().getEssentials();
			if (ess != null) {
				final User user = ess.getUser(player);
				user.setVanished(false);
			}
		} else {
			vanishHook.showPlayer(player);
		}

		if (showMessage) {
			player.sendMessage(ConfigGet.MESSAGES_VANISH_DISABLE.getString());
			// player.sendMessage(SpigotUtils.color("&cMode invisible désactivé."));
		}
	}

	@SuppressWarnings("deprecation")
	public void enable(final Player player, final boolean showMessage) {

		final VanishHook vanishHook = VanishHook.getInstance();
		if (!vanishHook.isEnabled()) {
			Permission.ADMIN_SEEVANISHED.getOnlinePlayersOpositeStream().forEach(noAdmin -> noAdmin.hidePlayer(player));
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false), true);
			player.spigot().setCollidesWithEntities(false);
			addVanishMetadata(player);

			final EssentialsHook essHook = EssentialsHook.getInstance();
			if (essHook.isEssEnabled()) {
				final User user = essHook.getEssentials().getUser(player);
				user.setVanished(true);
			}
		} else {
			vanishHook.hidePlayer(player);
		}

		if (showMessage) {
			player.sendMessage(ConfigGet.MESSAGES_VANISH_ENABLE.getString());
			// player.sendMessage(SpigotUtils.color("&aMode invisible activé."));
		}
	}

}
