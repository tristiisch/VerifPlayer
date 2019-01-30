package fr.tristiisch.verifplayer.utils.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiTools {

	private static Map<UUID, GuiTool> data = new HashMap<>();
	
	public static Map<UUID, GuiTool> getData() {
		return data;
	}
	
	public static GuiTool get(UUID uuid) {
		return data.get(uuid);
	}
	
	public static boolean contains(UUID uuid) {
		return data.containsKey(uuid);
	}
	
	public static void add(UUID uuid, GuiTool guiTool) {
		data.put(uuid, guiTool);
	}

	public static void remove(UUID uuid) {
		data.remove(uuid);
	}
	
}
