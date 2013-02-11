package game.skill;

import com.dd.plist.NSNumber;

public abstract class IStartTime extends ICommand {

	public double getStartTime() {
		Object obj = dic.objectForKey("startTime");
		if (obj == null)
			return 0;
		return Double.valueOf(obj.toString());
	}

	public void setStartTime(double startTime) {
		dic.put("startTime", new NSNumber(startTime));
	}
}
