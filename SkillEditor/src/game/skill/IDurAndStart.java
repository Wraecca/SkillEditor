package game.skill;

import com.dd.plist.NSNumber;

public abstract class IDurAndStart extends IStartTime{

	public double getDuration() {
		try{
			return Double.valueOf(dic.objectForKey("duration").toString());
		}
		catch(Exception e){
			return 0;
		}
		
	}

	public void setDuration(double duration) {
		dic.put("duration", new NSNumber(duration));
	}
}
