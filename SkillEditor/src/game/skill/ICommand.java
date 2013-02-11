package game.skill;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;

public abstract class ICommand {

	protected NSDictionary dic;

	public abstract double getStartTime();

	public abstract double getEndTime();

	public NSObject getNSObject(){
		return dic;
	}

	public abstract String getCmdType();

	public int getOrder() {
		if (dic == null || dic.objectForKey("order") == null)
			return 0;
		NSNumber num = (NSNumber) dic.objectForKey("order");
		return num.intValue();
	}

	public void setOrder(int order) {
		if (dic == null)
			return;
		dic.put("order", new NSNumber(order));
	}

}
