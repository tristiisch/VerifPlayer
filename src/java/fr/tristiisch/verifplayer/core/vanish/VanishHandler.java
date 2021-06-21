package fr.tristiisch.verifplayer.core.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.hook.EssentialsHook;
import fr.tristiisch.verifplayer.hook.VanishHook;
import fr.tristiisch.verifplayer.utils.VersionUtils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

@SuppressWarnings("deprecation")
public class VanishHandler extends VanishAbstract {

	public void disable(Player player, boolean showMessage) {
		VerifPlayerPlugin plugin = VerifPlayerPlugin.getInstance();
		VanishHook vanishHook = VanishHook.getInstance();
		if (!vanishHook.isEnabled()) {
			removeVanishMetadata(player);
			boolean isShowPlayerDeprecated = VersionUtils.ServerVersion.V1_14.isEqualOrOlder();
			if (isShowPlayerDeprecated)
				Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(plugin, player));
			else
				Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
			player.removePotionEffect(PotionEffectType.INVISIBILITY);

			if (VersionUtils.ServerVersion.V1_9.isEqualOrOlder())
				player.setCollidable(true);
			else
				player.spigot().setCollidesWithEntities(true);
			Essentials ess = EssentialsHook.getInstance().getEssentials();
			if (ess != null) {
				User user = ess.getUser(player);
				user.setVanished(false);
			}
		} else
			vanishHook.showPlayer(player);

		if (showMessage)
			player.sendMessage(ConfigGet.MESSAGES_VANISH_DISABLE.getString());
	}

	public void enable(Player player, boolean showMessage) {
		VerifPlayerPlugin plugin = VerifPlayerPlugin.getInstance();
		VanishHook vanishHook = VanishHook.getInstance();
		if (!vanishHook.isEnabled()) {
			addVanishMetadata(player);
			boolean isShowPlayerDeprecated = VersionUtils.ServerVersion.V1_14.isEqualOrOlder();
			if (isShowPlayerDeprecated)
				Permission.ADMIN_SEEVANISHED.getOnlinePlayersOpositeStream().forEach(noAdmin -> noAdmin.hidePlayer(plugin, player));
			else
				Permission.ADMIN_SEEVANISHED.getOnlinePlayersOpositeStream().forEach(noAdmin -> noAdmin.hidePlayer(player));
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false), true);
			if (VersionUtils.ServerVersion.V1_9.isEqualOrOlder())
				player.setCollidable(false);
			else
				player.spigot().setCollidesWithEntities(false);
			EssentialsHook essHook = EssentialsHook.getInstance();
			if (essHook.isEssEnabled()) {
				User user = essHook.getEssentials().getUser(player);
				user.setVanished(true);
			}
		} else
			vanishHook.hidePlayer(player);
		if (showMessage)
			player.sendMessage(ConfigGet.MESSAGES_VANISH_ENABLE.getString());
	}

}
