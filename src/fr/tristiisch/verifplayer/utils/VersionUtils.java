package fr.tristiisch.verifplayer.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;

import fr.tristiisch.verifplayer.VerifPlayer;

public class VersionUtils {

	public enum ServerVersion {

		V1_19(19),
		V1_18(18),
		V1_17(17),
		V1_16(16),
		V1_15(15),

		V1_14(14),
		V1_13(13),
		V1_12(12),
		V1_11(11),
		V1_10(10),
		V1_9(9),
		V1_8(8),
		V1_7(7),
		V1_6(6),
		V1_5(5),
		V1_4(4);

		int number;

		private ServerVersion(final int number) {
			this.number = number;
		}

		public float getFloat() {
			return this.number;
		}

		public String getName() {
			return this.toString().replace("V", "").replace("_", ".");
		}

		public boolean isEqualOrOlder() {
			return VersionUtils.INSTANCE.version.getFloat() >= this.getFloat();
		}

		public boolean isOlder() {
			return VersionUtils.INSTANCE.version.getFloat() > this.getFloat();
		}

		public boolean isTheSame() {
			return this.getFloat() == VersionUtils.INSTANCE.version.getFloat();
		}

		public boolean isYounger() {
			return VersionUtils.INSTANCE.version.getFloat() < this.getFloat();
		}
	}

	private static VersionUtils INSTANCE;

	public static VersionUtils getInstance() {
		return INSTANCE;
	}

	public final ServerVersion version = this.getVersion();
	private final ServerVersion maxVersion = ServerVersion.V1_13;
	private final ServerVersion minVersion = ServerVersion.V1_8;

	public VersionUtils(final VerifPlayer plugin) {
		VersionUtils.INSTANCE = this;
		if(this.version == null) {

			plugin.sendMessage("§cUnable to detect the server version, some features will generate errors. Server Version: " + this.getServerPackageVersion());

		} else if(this.maxVersion.isOlder()) {

			final SpigotUpdater spigotUpdater = SpigotUpdater.getInstance();
			spigotUpdater.setCompatibleServerVersion(false);

			plugin.sendMessage("§cThe maximum server version planned is " + this.maxVersion
					.getName() + ", so some features may not work. Maximum server version: " + this.maxVersion.getName());

		} else if(this.minVersion.isYounger()) {

			final SpigotUpdater spigotUpdater = SpigotUpdater.getInstance();
			spigotUpdater.setCompatibleServerVersion(false);

			plugin.sendMessage("§cThe plugin version is NOT compatible with this server version. The plugin has been disabled. Minimum server version: " + this.minVersion.getName());
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}

	private String getServerPackageVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1).replace("_", ".");
	}

	private ServerVersion getVersion() {
		final String serverPackageVersion = this.getServerPackageVersion();
		return Arrays.stream(ServerVersion.values()).filter(version -> serverPackageVersion.startsWith(version.getName())).findFirst().orElse(null);
	}

}
