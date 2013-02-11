package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;

public class CmdCamera extends IDurAndStartAndSelf {

	public CmdCamera(NSDictionary dic) {
		this.dic = dic;
	}
	
	public boolean isCenter() {
		NSNumber num = (NSNumber) dic.objectForKey("isCenter");
		return num.boolValue();
	}

	public void setCenter(boolean isCenter) {
		dic.put("isCenter", new NSNumber(isCenter));
	}

	public double getZoom() {
		return Double.valueOf(dic.objectForKey("zoom").toString());
	}

	public void setZoom(double zoom) {
		dic.put("zoom", new NSNumber(zoom));
	}

	public int getTarget() {
		if (isCenter())
			return 2;
		if (isSelf())
			return 0;
		else
			return 1;
	}

	public void setTarget(int target) {
		setSelf(false);
		setCenter(false);
		
		if (target == 0)
			setSelf(true);
		if (target == 1)
			setSelf(false);
		if (target == 2)
			setCenter(true);
	}

	public double getEndTime() {
		return getStartTime() + getDuration();
	}

	public String getCmdType() {
		return Skill.CMD_CAMERA;
	}

}
