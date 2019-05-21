package fr.tristiisch.verifplayer.core.vanish;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import fr.tristiisch.verifplayer.hook.EssentialsHook;
import fr.tristiisch.verifplayer.utils.SpigotUtils;

public class Vanish {

	private static List<Player> vanish = new ArrayList<>();

	public static void addVanish(final Player player) {
		vanish.add(player);
	}

	@SuppressWarnings("deprecation")
	public static void disable(final Player player, final boolean showMessage) {
		for(final Player players : Bukkit.getOnlinePlayers()) {
			players.showPlayer(player);
		}
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.spigot().setCollidesWithEntities(true);

		final Essentials ess = EssentialsHook.getInstance().getEssentials();
		if(ess != null) {
			final User user = ess.getUser(player);
			user.setVanished(false);
		}
		removeVanish(player);
		if(showMessage) {
			player.sendMessage(SpigotUtils.color("&cMode invisible désactivé."));
		}
	}

	@SuppressWarnings("deprecation")
	public static void enable(final Player player, final boolean showMessage) {

		/*		for(final Player target : Bukkit.getOnlinePlayers()) {
					final EmeraldPlayer emeraldTarget = new AccountProvider(target.getUniqueId()).getEmeraldPlayer();
					if(emeraldTarget == null || emeraldTarget.hasPowerLessThan(EmeraldGroup.ADMIN)) {
						target.hidePlayer(player);
					}
				}*/
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false), true);
		player.spigot().setCollidesWithEntities(false);
		/* GhostUtils.ghostFactory.addPlayer(player); */
		final Essentials ess = EssentialsHook.getInstance().getEssentials();
		if(ess != null) {
			final User user = ess.getUser(player);
			user.setVanished(true);
		}
		addVanish(player);
		if(showMessage) {
			player.sendMessage(SpigotUtils.color("&aMode invisible activé."));
		}
	}

	public static List<Player> getVanish() {
		return vanish;
	}

	public static boolean isVanish(final Player player) {
		return vanish.contains(player);
	}

	public static void removeVanish(final Player player) {
		vanish.remove(player);
	}
}
