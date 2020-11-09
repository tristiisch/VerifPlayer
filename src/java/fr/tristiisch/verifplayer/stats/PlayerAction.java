package fr.tristiisch.verifplayer.stats;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.Utils;

public class PlayerAction implements Cloneable {

	public static PlayerAction WALK = new PlayerAction("Walk", "Player move (walk, swim, boat ...)");
	public static PlayerAction PVP = new PlayerAction("PvP", "PvP vs ");
	public static PlayerAction PVE = new PlayerAction("PvE", "PvE vs ");
	public static PlayerAction FREEZE = new PlayerAction("Freezed", "Player has been frozen by ");
	public static PlayerAction VERIFSPEC = new PlayerAction("VerifSpec", "Mod use /verifspec tools");
	public static PlayerAction VERIFGUI = new PlayerAction("VerifGui", "Mod use /verif gui");
	public static PlayerAction MINING_DIAMANT = new PlayerAction("Diamant", "Player mining diamants ore");
	public static PlayerAction GUI_OPEN = new PlayerAction("In GUI", "Player is in ");

	String name;
	String description;
	Integer amont;
	Location location;
	long createAction;
	long lastUpdate;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public PlayerAction create() {
		createAction = Utils.getCurrentTimeinSeconds();
		try {
			return (PlayerAction) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PlayerAction appendDescription(String append) {
		description += " " + append;
		return this;
	}

	public PlayerAction incrementAmount(String append) {
		if (amont == null)
			amont = 0;
		amont++;
		return this;
	}

	public PlayerAction updatePlayer(Player player) {
		lastUpdate = Utils.getCurrentTimeinSeconds();
		location = player.getLocation();
		return this;
	}

	public PlayerAction updatePlayer(Player player, PlayerAction playerAction) {
		updatePlayer(player);
		amont = playerAction.getAmont();
		name = playerAction.getName();
		description = playerAction.getDescription();
		return this;
	}

	public Integer getAmont() {
		return amont;
	}

	public Location getLocation() {
		return location;
	}

	public String getWorldName() {
		return location.getWorld().getName();
	}

	public long getCreateAction() {
		return createAction;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public PlayerAction(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public PlayerAction(String name, String description, Integer amont, Location location, long createAction, long lastUpdate) {
		this.name = name;
		this.description = description;
		this.amont = amont;
		this.location = location;
		this.createAction = createAction;
		this.lastUpdate = lastUpdate;
	}

	public PlayerAction(Player player, String name, String description, Integer amont, long lastUpdate) {
		this.name = name;
		this.description = description;
		this.amont = amont;
		createAction = Utils.getCurrentTimeinSeconds();
		this.lastUpdate = createAction;
		location = player.getLocation();
	}

}
