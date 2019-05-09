package fr.tristiisch.verifplayer.gui.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.utils.Utils;

public class GuiCreator {

	public static int nbBoxPerRow = 9;
	private final GuiPage guiPage;
	private String data;
	private Map<Integer, ItemStack> items = new HashMap<>();

	private boolean missClickClosing = false;
	private boolean clickOnItems = false;

	public GuiCreator(final GuiPage guiPage) {
		this.guiPage = guiPage;
	}

	public GuiCreator(final GuiPage guiPage, final String data) {
		this.guiPage = guiPage;
		this.data = data;
	}

	public void addItem(final int slot, final ItemStack item) {
		this.items.put(slot, item);
	}

	public String getData() {
		return this.data;
	}

	public GuiPage getGuiPage() {
		return this.guiPage;
	}

	private Inventory getInventory() {
		final Inventory inventory = Bukkit.createInventory(null, this.guiPage.getSize(), Utils.color(this.guiPage.getTitle()));
		if(!this.items.isEmpty()) {
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

	public void openGui(final Player player) {
		player.openInventory(this.getInventory());
	}

	public void setClickOnItems(final boolean clickOnItems) {
		this.clickOnItems = clickOnItems;
	}

	public void setData(final String data) {
		this.data = data;
	}

	public void setItems(final Map<Integer, ItemStack> items) {
		this.items = items;
	}

	public void setMissClickClosing(final boolean missClickClosing) {
		this.missClickClosing = missClickClosing;
	}
}
