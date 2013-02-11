package game.skill.bar;

import game.skill.ICommand;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;

public class CmdTimeBarRow extends DefaultTimeBarRowModel {

	CmdInterval interval;

	public CmdTimeBarRow(ICommand cmd) {
		super();
		setRowHeader(new DefaultRowHeader(""));
		interval = new CmdInterval(cmd);
		addInterval(interval);
	}

	public void recalculateTime() {
		interval.recalculateTime();
	}
	
	public ICommand getCmd(){
		return interval.getCmd();
	}
}
