package fr.tristiisch.verifplayer.core.vanish;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.tristiisch.verifplayer.Main;

@SuppressWarnings("deprecation")
public class GhostUtils {

	// No players in the ghost factory
	private static final OfflinePlayer[] EMPTY_PLAYERS = new OfflinePlayer[0];
	/**
	 * Team of ghosts and people who can see ghosts.
	 */
	private static final String GHOST_TEAM_NAME = "Ghosts";

	public static GhostUtils ghostFactory;
	private static final long UPDATE_DELAY = 20L;

	static {
		// In your onEnable function
		ghostFactory = new GhostUtils(Main.getInstance());
	}
	private boolean closed;

	// Players that are actually ghosts
	private final Set<String> ghosts = new HashSet<>();
	private Team ghostTeam;

	// Task that must be cleaned up
	private BukkitTask task;

	public GhostUtils(final Plugin plugin) {
		// Initialize
		this.createTask(plugin);
		this.createGetTeam();
	}

	/**
	 * Add the given player to this ghost manager. This ensures that it can see
	 * ghosts, and later become one.
	 *
	 * @param player
	 *            - the player to add to the ghost manager.
	 */
	public void addPlayer(final Player player) {
		this.validateState();
		if(!this.ghostTeam.hasPlayer(player)) {
			this.ghostTeam.addPlayer(player);
			this.ghosts.add(player.getName());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false), true);
		}
	}

	/**
	 * Remove all existing player members and ghosts.
	 */
	public void clearMembers() {
		if(this.ghostTeam != null) {
			for(final OfflinePlayer player : this.getMembers()) {
				this.ghostTeam.removePlayer(player);
			}
		}
	}

	public void close() {
		if(!this.closed) {
			this.task.cancel();
			this.ghostTeam.unregister();
			this.closed = true;
		}
	}

	private void createGetTeam() {
		final Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();

		this.ghostTeam = board.getTeam(GHOST_TEAM_NAME);

		// Create a new ghost team if needed
		if(this.ghostTeam == null) {
			this.ghostTeam = board.registerNewTeam(GHOST_TEAM_NAME);
		}
		// Thanks to Rprrr for noticing a bug here
		this.ghostTeam.setCanSeeFriendlyInvisibles(true);
		this.ghostTeam.setNameTagVisibility(NameTagVisibility.NEVER);
	}

	private void createTask(final Plugin plugin) {
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			for(final OfflinePlayer member : GhostUtils.this.getMembers()) {
				final Player player = member.getPlayer();

				if(player != null) {
					// Update invisibility effect
					GhostUtils.this.setGhost(player, GhostUtils.this.isGhost(player));
				} else {
					GhostUtils.this.ghosts.remove(member.getName());
					GhostUtils.this.ghostTeam.removePlayer(member);
				}
			}
		}, UPDATE_DELAY, UPDATE_DELAY);
	}

	/**
	 * Retrieve every ghost currently tracked by this manager.
	 *
	 * @return Every tracked ghost.
	 */
	public OfflinePlayer[] getGhosts() {
		this.validateState();
		final Set<OfflinePlayer> players = new HashSet<>(this.ghostTeam.getPlayers());

		// Remove all non-ghost players
		for(final Iterator<OfflinePlayer> it = players.iterator(); it.hasNext();) {
			if(!this.ghosts.contains(it.next().getName())) {
				it.remove();
			}
		}
		return this.toArray(players);
	}

	/**
	 * Retrieve every ghost and every player that can see ghosts.
	 *
	 * @return Every ghost or every observer.
	 */
	public OfflinePlayer[] getMembers() {
		this.validateState();
		return this.toArray(this.ghostTeam.getPlayers());
	}

	/**
	 * Determine if the current player is tracked by this ghost manager, or is a
	 * ghost.
	 *
	 * @param player
	 *            - the player to check.
	 * @return TRUE if it is, FALSE otherwise.
	 */
	public boolean hasPlayer(final Player player) {
		this.validateState();
		return this.ghostTeam.hasPlayer(player);
	}

	public boolean isClosed() {
		return this.closed;
	}

	/**
	 * Determine if the given player is tracked by this ghost manager and is a
	 * ghost.
	 *
	 * @param player
	 *            - the player to test.
	 * @return TRUE if it is, FALSE otherwise.
	 */
	public boolean isGhost(final Player player) {
		return player != null && this.hasPlayer(player) && this.ghosts.contains(player.getName());
	}

	/**
	 * Remove the given player from the manager, turning it back into the living and
	 * making it unable to see ghosts.
	 *
	 * @param player
	 *            - the player to remove from the ghost manager.
	 */
	public void removePlayer(final Player player) {
		this.validateState();
		if(this.ghostTeam.removePlayer(player)) {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	/**
	 * Set wheter or not a given player is a ghost.
	 *
	 * @param player
	 *            - the player to set as a ghost.
	 * @param isGhost
	 *            - TRUE to make the given player into a ghost, FALSE otherwise.
	 */
	public void setGhost(final Player player, final boolean isGhost) {
		// Make sure the player is tracked by this manager
		if(!this.hasPlayer(player)) {
			this.addPlayer(player);
		}

		if(isGhost) {
			this.ghosts.add(player.getName());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false), true);
		} else if(!isGhost) {
			this.ghosts.remove(player.getName());
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	private OfflinePlayer[] toArray(final Set<OfflinePlayer> players) {
		if(players != null) {
			return players.toArray(new OfflinePlayer[0]);
		} else {
			return EMPTY_PLAYERS;
		}
	}

	private void validateState() {
		if(this.closed) {
			throw new IllegalStateException("Ghost factory has closed. Cannot reuse instances.");
		}
	}
}