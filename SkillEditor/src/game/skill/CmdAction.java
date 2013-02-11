package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;

public class CmdAction extends IStartTime {

	private SkillPlist pList;

	public CmdAction(NSDictionary dic, SkillPlist pList) {
		this.dic = dic;
		this.pList = pList; 
	}

	public boolean isSelf() {
		NSNumber num = (NSNumber) dic.objectForKey("isSelf");
		return num.boolValue();
	}

	public void setSelf(boolean isSelf) {
		dic.put("isSelf", new NSNumber(isSelf));
	}

	public String getBehavior() {
		return dic.objectForKey("behavior").toString();
	}

	public void setBehavior(String behavior) {
		dic.put("behavior", new NSString(behavior));
	}

	public double getDelay() {
		return Double.valueOf(dic.objectForKey("delay").toString());
	}

	public void setDelay(double delay) {
		dic.put("delay", new NSNumber(delay));
	}
	
	public void setReferenceRole(String role) {
		dic.put("referenceRole", new NSString(role));
	}

	public String getRefereneRole() {
		if (dic.objectForKey("referenceRole") == null)
			return "null";
		return dic.objectForKey("referenceRole").toString();
	}

	public double getEndTime() {
		int frameCount = pList.getFrameCount(getRefereneRole(), getBehavior());
		return getStartTime() + getDelay() * frameCount;
	}

	public String getCmdType() {
		return Skill.CMD_ACTION;
	}
}
