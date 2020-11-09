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
		items.put(slot, item);
	}

	public void addItem(ItemStack item) {
		int i = 0;
		while (items.get(i) != null)
			i++;
		items.put(i, item);
	}

	public String getData() {
		return data;
	}

	public GuiPage getGuiPage() {
		return guiPage;
	}

	private Inventory getInventory() {
		Inventory inventory = Bukkit.createInventory(null, guiPage.getSize(), SpigotUtils.color(guiPage.getTitle()));
		if (!items.isEmpty())
			items.entrySet().forEach(entry -> inventory.setItem(entry.getKey(), entry.getValue()));
		return inventory;
	}

	public boolean isClickOnItems() {
		return clickOnItems;
	}

	public boolean isMissClickClosing() {
		return missClickClosing;
	}

	public void openGui(Player player) {
		player.openInventory(getInventory());
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
