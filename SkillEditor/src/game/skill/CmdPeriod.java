package game.skill;

import com.dd.plist.NSDictionary;

public class CmdPeriod extends IDurAndStart {

	public CmdPeriod(NSDictionary dic) {
		this.dic = dic;
	}

	public double getStartTime() {
		return 0;
	}
	
	public void setStartTime(double startTime) {
	}

	public double getEndTime() {
		return getStartTime() + getDuration();
	}

	public String getCmdType() {
		return Skill.CMD_PERIOD;
	}
	
	
}
