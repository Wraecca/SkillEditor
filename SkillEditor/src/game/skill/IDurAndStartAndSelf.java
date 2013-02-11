package game.skill;

import com.dd.plist.NSNumber;

public abstract class IDurAndStartAndSelf extends IDurAndStart{

	public boolean isSelf() {
		NSNumber num = (NSNumber) dic.objectForKey("isSelf");
		return num.boolValue();
	}

	public void setSelf(boolean isSelf) {
		dic.put("isSelf", new NSNumber(isSelf));
	}
}
