package fr.tristiisch.verifplayer.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;

import fr.tristiisch.verifplayer.Main;

public class VersionUtils {

	public enum ServerVersion {

		V1_19(119),
		V1_18(118),
		V1_17(117),
		V1_16(116),
		V1_15(115),

		V1_14(114),
		V1_13(113),
		V1_12(112),
		V1_11(111),
		V1_10(110),
		V1_9(19),
		V1_8(18),
		V1_7(17),
		V1_6(16),
		V1_5(15),
		V1_4(14);

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
			return VersionUtils.INSTANCE.version.getFloat() <= this.getFloat();
		}

		public boolean isOlder() {
			return VersionUtils.INSTANCE.version.getFloat() < this.getFloat();
		}

		public boolean isTheSame() {
			return this.getFloat() == VersionUtils.INSTANCE.version.getFloat();
		}

		public boolean isYounger() {
			return this.getFloat() < VersionUtils.INSTANCE.version.getFloat();
		}
	}

	private static VersionUtils INSTANCE;

	private final ServerVersion version = this.getVersion();
	private final ServerVersion maxVersion = ServerVersion.V1_13;
	private final ServerVersion minVersion = ServerVersion.V1_8;

	public VersionUtils(final Main plugin) {
		VersionUtils.INSTANCE = this;
		if(this.version == null) {

			plugin.sendMessage("§cUnable to detect the server version, some features will generate errors. Server Version: " + this.getServerPackageVersion());

		} else if(this.maxVersion.isYounger()) {

			final SpigotUpdater spigotUpdater = SpigotUpdater.getInstance();
			spigotUpdater.setCompatibleServerVersion(false);

			plugin.sendMessage("§cThe maximum server version planned is " + this.maxVersion
					.getName() + ", so some features may not work. Maximum server version: " + this.maxVersion.getName());

		} else if(this.minVersion.isOlder()) {

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
