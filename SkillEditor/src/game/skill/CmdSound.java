package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;

public class CmdSound extends IStartTime {

	public CmdSound(NSDictionary dic) {
		this.dic = dic;
	}

	public double getEndTime() {
		return getStartTime() + 0.5;
	}

	public String getCmdType() {
		return Skill.CMD_SOUND;
	}
	
	public String getSoundFileName() {
		return dic.objectForKey("soundFileName").toString();
	}
	
	public void setSoundFileName(String soundFileName) {
		dic.put("soundFileName", new NSString(soundFileName));
	}
}
