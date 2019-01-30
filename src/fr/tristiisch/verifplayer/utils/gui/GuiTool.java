package fr.tristiisch.verifplayer.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import fr.tristiisch.verifplayer.utils.Utils;

public class GuiTool {

	GuiName guiName;
	Inventory inventory;
	Object data;
	
	public GuiTool(GuiName name, String title, int size) {
		this.guiName = name;
		title = Utils.color(title);
		this.inventory = Bukkit.createInventory(null, size, title);
	}

	public GuiTool(GuiName guiName, String title, int size, Object data) {
		this.guiName = guiName;
		title = Utils.color(title);
		this.inventory = Bukkit.createInventory(null, size, title);
		this.data = data;
	}

	public GuiTool(Inventory inventory) {
		this.guiName = GuiName.OTHER;
		this.inventory = inventory;
	}
	
	public String getTitle() {
		return inventory.getTitle();
	}
	
	public InventoryType getInventoryType() {
		return inventory.getType();
	}

	public GuiName getGuiName() {
		return guiName;
	}

	public int getSize() {
		return inventory.getSize();
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public void open(Player player) {
		player.openInventory(inventory);
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
