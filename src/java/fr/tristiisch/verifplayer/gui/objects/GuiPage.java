package fr.tristiisch.verifplayer.gui.objects;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;

public /* abstract */ class GuiPage implements Cloneable {

	private String id;
	private String title;
	private String description;
	private int rowSize;

	private Map<GuiPageVariable, PlayerInfo> variables = null;

	public GuiPage(String title, String description, int rowSize) {
		this.id = title.toLowerCase();
		this.title = title;
		this.description = description;
		this.rowSize = rowSize;
	}

	public GuiPage(String title, String description, int rowSize, GuiPageVariable... variables) {
		this.id = title.toLowerCase();
		this.title = title;
		this.description = description;
		this.rowSize = rowSize;
		if (variables != null) {
			this.variables = IntStream.range(0, variables.length - 1).boxed().collect(Collectors.toMap(i -> variables[i], i -> null));
		}
	}

	@Override
	public GuiPage clone() {
		try {
			return (GuiPage) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDescription() {
		return this.remplaceVariables(this.description);
	}

	public int getRowSize() {
		return this.rowSize;
	}

	public int getSize() {
		return this.rowSize * GuiCreator.nbBoxPerRow;
	}

	public String getTitle() {
		return this.remplaceVariables(this.title);
	}

	public Map<GuiPageVariable, PlayerInfo> getVariables() {
		return this.variables;
	}

	public String getVariablesNames() {
		return String.join(", ", this.variables.keySet().stream().map(GuiPageVariable::getName).collect(Collectors.toSet()));
	}

	public boolean isSamePage(GuiPage guiPage) {
		return this.id.equals(guiPage.id);
	}

	/*
	 * public void replaceTitleVariable(GuiPageVariable guiPageVariable, final
	 * String replacement) { this.title = this.replaceVariable(this.title,
	 * guiPageVariable, replacement); }
	 * 
	 * public String replaceVariable(String s, GuiPageVariable
	 * guiPageVariable) { return this.replaceVariable(s, guiPageVariable,
	 * guiPageVariable.getResult()); }
	 * 
	 * private String replaceVariable(String s, GuiPageVariable
	 * guiPageVariable, String replacement) {
	 * if(this.variables.contains(guiPageVariable)) { s =
	 * s.replaceAll(guiPageVariable.getRegex(), replacement); return s; } else {
	 * VerifPlayerPlugin.getInstance().getLogger().log(Level.WARNING,
	 * "DEV ERROR: The page called '" + this.getTitle() +
	 * "' does not use a variable " + guiPageVariable.getName() + "!"); return null;
	 * } }
	 */

	private String remplaceVariables(String s) {

		for (Entry<GuiPageVariable, PlayerInfo> entry : this.variables.entrySet()) {
			GuiPageVariable variable = entry.getKey();
			PlayerInfo playerInfo = entry.getValue();
			s = variable.changeString(s, playerInfo);
		}
		return s;
	}

	public void setVariable(GuiPageVariable variable, PlayerInfo playerInfo) {
		// if(!this.variables.containsKey(variable)) {
		// new Exception("There is no GuiPageVariable." + variable.toString() + " set in
		// the VerifPlayerGui " + this.title).printStackTrace();
		// }
		this.variables.put(variable, playerInfo);
	}
}
