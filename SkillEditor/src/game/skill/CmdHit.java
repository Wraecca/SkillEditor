package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;

public class CmdHit extends IStartTime {

	public CmdHit(NSDictionary dic) {
		this.dic = dic;
	}

	public boolean isSelf() {
		NSNumber num = (NSNumber) dic.objectForKey("isSelf");
		return num.boolValue();
	}

	public void setSelf(boolean isSelf) {
		dic.put("isSelf", new NSNumber(isSelf));
	}

	public int getFontSize() {
		return Integer.valueOf(dic.objectForKey("fontSize").toString());
	}

	public void setFontSize(int fontSize) {
		dic.put("fontSize", new NSNumber(fontSize));
	}

	public int getTimes() {
		return Integer.valueOf(dic.objectForKey("times").toString());
	}

	public void setTimes(int times) {
		dic.put("times", new NSNumber(times));
	}

	public double getInterval() {
		return Double.valueOf(dic.objectForKey("interval").toString());
	}

	public void setInterval(double interval) {
		dic.put("interval", new NSNumber(interval));
	}

	public double getEndTime() {
		if (getTimes() == 1)
			return getStartTime() + 0.3f;
		return getStartTime() + getInterval()*(getTimes()-1);
	}

	public String getCmdType() {
		return Skill.CMD_HIT;
	}
	
	
}
