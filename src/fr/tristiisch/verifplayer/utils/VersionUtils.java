package fr.tristiisch.verifplayer.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;

public class VersionUtils {

	public enum Versions {

		V1_19(1.19f),
		V1_18(1.18f),
		V1_17(1.17f),
		V1_16(1.16f),
		V1_15(1.15f),

		V1_14(1.14f),
		V1_13(1.13f),
		V1_12(1.12f),
		V1_11(1.11f),
		V1_10(1.10f),
		V1_9(1.9f),
		V1_8(1.8f),
		V1_7(1.7f);

		Float number;

		private Versions(final float number) {
			this.number = number;
		}

		public float getFloat() {
			return this.number;
		}

		public String getName() {
			return String.valueOf(this.number);
		}

		public boolean isEqualOrOlder() {
			return VersionUtils.version.getFloat() <= this.getFloat();
		}

		public boolean isOlder() {
			return VersionUtils.version.getFloat() < this.getFloat();
		}

		public boolean isTheSame() {
			return this.getFloat() == VersionUtils.version.getFloat();
		}

		public boolean isYounger() {
			return VersionUtils.version.getFloat() > this.getFloat();
		}
	}

	public static Versions version = getVersion();

	public static Versions getVersion() {
		final String serverPackageVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1).replace("_", ".");
		return Arrays.stream(Versions.values()).filter(version -> serverPackageVersion.startsWith(version.getName())).findFirst().orElse(null);
	}

}
