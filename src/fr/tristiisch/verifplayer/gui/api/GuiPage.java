package fr.tristiisch.verifplayer.gui.api;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.tristiisch.verifplayer.Main;

public abstract class GuiPage {

	public enum GuiPageVariable {
		PLAYER,
		ATTACKER,
		VICTIM,
		MODERATOR,
		ARG1,
		ARG2,
		ARG3,
		ARG4;

		public String getName() {
			return this.toString().toLowerCase();
		}

		public String getRegex() {
			return "\\%" + this.getName() + "\\%";
		}
	}

	private final String id;
	private String title;
	private final String description;
	private final int rowSize;

	private List<GuiPageVariable> variables;

	public GuiPage(final String title, final String description, final int rowSize) {
		this.id = title.toLowerCase();
		this.title = title;
		this.description = description;
		this.rowSize = rowSize;
	}

	public GuiPage(final String title, final String description, final int rowSize, final GuiPageVariable... variables) {
		this.id = title.toLowerCase();
		this.title = title;
		this.description = description;
		this.rowSize = rowSize;
		this.variables = Arrays.asList(variables);
	}

	public String getDescription() {
		return this.description;
	}

	public int getRowSize() {
		return this.rowSize;
	}

	public int getSize() {
		return this.rowSize * GuiCreator.nbBoxPerRow;
	}

	public String getTitle() {
		return this.title;
	}

	public List<GuiPageVariable> getVariables() {
		return this.variables;
	}

	public String getVariablesNames() {
		return String.join(", ", this.variables.stream().map(GuiPageVariable::getName).collect(Collectors.toSet()));
	}

	public boolean isSamePage(final GuiPage guiPage) {
		return this.id.equals(guiPage.id);
	}

	public void replaceTitleVariable(final GuiPageVariable guiPageVariable, final String replacement) {
		this.title = this.replaceVariable(this.title, guiPageVariable, replacement);
	}

	public String replaceVariable(String s, final GuiPageVariable guiPageVariable, final String replacement) {
		if(this.variables.contains(guiPageVariable)) {
			s = s.replaceAll(guiPageVariable.getRegex(), replacement);
			return s;
		} else {
			Main.getInstance().getLogger().log(Level.WARNING, "DEV ERROR: The page called '" + this.getTitle() + "' does not use a variable " + guiPageVariable.getName() + "!");
			return null;
		}
	}

	public void replaceVariable(final String variableName, final String replacement) {
		this.title = this.title.replaceAll("\\%" + variableName + "\\%", replacement);
	}

	public void setTitle(final String title) {
		this.title = title;
	}
}
