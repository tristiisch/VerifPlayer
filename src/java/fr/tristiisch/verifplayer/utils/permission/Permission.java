package fr.tristiisch.verifplayer.utils.permission;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public enum Permission {

	MODERATOR_COMMAND_VERIF("Use /verif <player>"),
	MODERATOR_COMMAND_ALERTCPS("Toggle CPS alert with /alertcps"),
	ADMIN_RECEIVEALERT("Receive CPS alerts from itself"),
	MODERATOR_RECEIVEALERT("Receive CPS alerts"),
	MODERATOR_COMMAND_VERIFSPEC,
	MODERATOR_COMMAND_VERIFVANISH,
	MODERATOR_COMMAND_FREEZE,
	MODERATOR_RECEIVEFREEZEDISCONNECTED,
	MODERATOR_SEEFREEZEMSG,
	ADMIN_SEEVANISHED,
	ADMIN_CANTVERIF,
	ADMIN_COMMAND_RELOAD;

	public static boolean isAuthor(Player player) {
		return player.getUniqueId().toString().equals("ca0a1663-1696-4d62-b93f-281965522a76");
	}

	String id;

	String description;

	private Permission() {
		StringBuilder sb = new StringBuilder();
		sb.append(VerifPlayerPlugin.getInstance().getDescription().getName());
		sb.append(".");
		sb.append(toString().replace("_", "."));
		id = sb.toString().toLowerCase();
	}

	private Permission(String description) {
		this();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public Set<Player> getOnlinePlayers() {
		return getOnlinePlayersStream().collect(Collectors.toSet());
	}

	public Stream<? extends Player> getOnlinePlayersOpositeStream() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> !hasPermission(player));
	}

	public Stream<? extends Player> getOnlinePlayersStream() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> hasPermission(player));
	}

	public boolean hasPermission(CommandSender sender) {
		return sender.hasPermission(getId());
	}

	public void sendMessageToOnlinePlayers(String message) {
		getOnlinePlayersStream().forEach(p -> p.sendMessage(message));
		VerifPlayerPlugin.getInstance().sendMessage(message);
	}
}