package game.skill.bar;

import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;

public class CmdTimeBarModel extends DefaultTimeBarModel {

	public void removeAllRow() {
		for (int i = getRowCount() - 1; i >= 0; i--) {
			remRow(getRow(i));
		}
	}

	public void saveOrder() {
		for (int i = 0; i < getRowCount(); i++) {
			CmdTimeBarRow row = (CmdTimeBarRow) getRow(i);
			row.getCmd().setOrder(i);
		}
	}

	public void moveRow(CmdTimeBarRow row, int delta) {
		if (row == null)
			return;

		int idx = getIndexForRow(row) + delta;
		if (idx < 0 || idx >= getRowCount())
			return;
		remRow(row);
		addRow(idx, row);
	}
}
