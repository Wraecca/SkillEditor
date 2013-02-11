package game.skill;

import game.skill.ui.ErrorReporter;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;

public class CmdMove extends IDurAndStartAndSelf {

	public CmdMove(NSDictionary dic) {
		this.dic = dic;
	}

	public int getDestination() {
		return Integer.valueOf(dic.objectForKey("destination").toString());
	}

	public void setDestination(int destination, double custom) {
		dic.put("destination", new NSNumber(destination));
		if (destination == 3)
			setCustomMoveRange(custom);
		else
			setCustomMoveRange(0f);
	}
	
	public int getVertex() {
		try {
			return Integer.valueOf(dic.objectForKey("vertex").toString());
		} catch (Exception e) {
			ErrorReporter.getInstance().addError("move vertex does not exist");
			return 0;
		}
	}

	public void setVertex(int vertex) {
		dic.put("vertex", new NSNumber(vertex));
	}

	public double getEndTime() {
		return getStartTime() + getDuration();
	}
	
	public double getCustomMoveRange() {
		if (getDestination() == 3)
			return Double.valueOf(dic.objectForKey("customMoveRange").toString());
		else 
			return 0;
	}

	private void setCustomMoveRange(double customMoveRange) {
		dic.put("customMoveRange", new NSNumber(customMoveRange));
	}

	public String getCmdType() {
		return Skill.CMD_MOVE;
	}
}
