package fr.tristiisch.verifplayer.utils.gui.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tristiisch.verifplayer.utils.Utils;

public class GuiCreator {

	public static int nbBoxPerRow = 9;
	private final GuiPage guiPage;
	private String data;
	private boolean missClickClosing;

	private boolean clickOnItems;

	public GuiCreator(final GuiPage guiPage) {
		this.missClickClosing = false;
		this.clickOnItems = false;
		this.guiPage = guiPage;
	}

	public GuiCreator(final GuiPage guiPage, final String data) {
		this.missClickClosing = false;
		this.clickOnItems = false;
		this.guiPage = guiPage;
		this.data = data;
	}

	public String getData() {
		return this.data;
	}

	public GuiPage getGuiPage() {
		return this.guiPage;
	}

	private Inventory getInventory() {
		return Bukkit.createInventory(null, this.guiPage.getSize(), Utils.color(this.guiPage.getTitle()));
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

	public void setMissClickClosing(final boolean missClickClosing) {
		this.missClickClosing = missClickClosing;
	}
}
