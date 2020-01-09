package fr.tristiisch.verifplayer.gui.objects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.utils.SpigotUtils;

public class GuiCreator {

	public static int nbBoxPerRow = 9;
	private GuiPage guiPage;
	private String data;
	private Map<Integer, ItemStack> items = new HashMap<>();

	private boolean missClickClosing = false;
	private boolean clickOnItems = false;

	public GuiCreator(GuiPage guiPage) {
		this.guiPage = guiPage.clone();
	}

	public GuiCreator(GuiPage guiPage, String data) {
		this.guiPage = guiPage.clone();
		this.data = data;
	}

	public void addItem(int slot, ItemStack item) {
		this.items.put(slot, item);
	}

	public String getData() {
		return this.data;
	}

	public GuiPage getGuiPage() {
		return this.guiPage;
	}

	private Inventory getInventory() {
		Inventory inventory = Bukkit.createInventory(null, this.guiPage.getSize(), SpigotUtils.color(this.guiPage.getTitle()));
		if (!this.items.isEmpty()) {
			this.items.entrySet().forEach(entry -> inventory.setItem(entry.getKey(), entry.getValue()));
		}
		return inventory;
	}

	public boolean isClickOnItems() {
		return this.clickOnItems;
	}

	public boolean isMissClickClosing() {
		return this.missClickClosing;
	}

	public void openGui(Player player) {
		player.openInventory(this.getInventory());
	}

	public void setClickOnItems(boolean clickOnItems) {
		this.clickOnItems = clickOnItems;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setItems(Map<Integer, ItemStack> items) {
		this.items = items;
	}

	public void setMissClickClosing(boolean missClickClosing) {
		this.missClickClosing = missClickClosing;
	}
}
