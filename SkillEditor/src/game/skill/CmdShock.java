package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;

public class CmdShock extends IDurAndStart {
	
	public CmdShock(NSDictionary dic) {
		this.dic = dic;
	}

	public int getLevel() {
		return Integer.valueOf(dic.objectForKey("level").toString());
	}

	public void setLevel(int level) {
		dic.put("level", new NSNumber(level));
	}

	public double getEndTime() {
		return getStartTime() + getDuration();
	}

	public String getCmdType() {
		return Skill.CMD_SHOCK;
	}
}
