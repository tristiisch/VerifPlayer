package fr.tristiisch.verifplayer.utils.permission;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permission {

	MODERATOR_COMMAND_VERIF("Use /verif <player>"),
	MODERATOR_COMMAND_ALERTCPS("Toggle CPS alert with /alertcps"),
	MODERATOR_RECEIVEALERT("Receive CPS alerts"),
	ADMIN("Disable Moderator /verif & Allow /verifplayer <reload>"),
	MODERATOR_COMMAND_VERIFSPEC(),
	MODERATOR_COMMAND_VANISH(),
	MODERATOR_COMMAND_FREEZE,
	MODERATOR_RECEIVEFREEZEDISCONNECTED, 
	ADMIN_SEEVANISHED,
	;

	public static boolean isAuthor(final Player player) {
		return player.getUniqueId().toString().equals("ca0a1663-1696-4d62-b93f-281965522a76");
	}

	String id;

	String description;

	private Permission() {
		final StringBuilder sb = new StringBuilder();
		sb.append(PermissionManager.pluginName);
		sb.append(".");
		sb.append(this.toString().replaceAll("_", "."));
		this.id = sb.toString().toLowerCase();
	}

	private Permission(final String description) {
		this();
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public String getId() {
		return this.id;
	}

	public boolean hasPermission(final CommandSender sender) {
		return sender.hasPermission(this.getId());
	}
	
	public Stream<? extends Player> getOnlinePlayersStream() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> hasPermission(player));
	}
	
	public Stream<? extends Player> getOnlinePlayersOpositeStream() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> !hasPermission(player));
	}


	public Set<Player> getOnlinePlayers() {
		return getOnlinePlayersStream().collect(Collectors.toSet());
	}
	
	public void sendMessageToOnlinePlayers(String message) {
		getOnlinePlayersStream().forEach(p -> p.sendMessage(message));;
	}
}