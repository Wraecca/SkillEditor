package game.skill;

import game.skill.ui.ErrorReporter;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;

public class CmdEffect extends IDurAndStart {

	private SkillPlist pList;

	public CmdEffect(NSDictionary dic, SkillPlist pList) {
		this.dic = dic;
		this.pList = pList; 
	}

	public String getAnimation() {
		return dic.objectForKey("animation").toString();
	}

	public void setAnimation(String animation) {
		dic.put("animation", new NSString(animation));
	}

	public int getStartPoint() {
		return Integer.valueOf(dic.objectForKey("startPoint").toString());
	}

	public void setStartPoint(int startPoint) {
		dic.put("startPoint", new NSNumber(startPoint));
	}

	public int getEndPoint() {
		return Integer.valueOf(dic.objectForKey("endPoint").toString());
	}

	public void setEndPoint(int endPoint) {
		dic.put("endPoint", new NSNumber(endPoint));
	}

	public double getEndTime() {
		double dur = getDuration();
		if (dur == 0) {
			NSDictionary effect = pList.getEffectAnimation(getAnimation());
			int frame = Integer.valueOf(effect.objectForKey("frameCount").toString());
			double delay = Double.valueOf(effect.objectForKey("delay").toString());
			dur = frame * delay;
		}
		return getStartTime() + dur;
	}

	public String getCmdType() {
		return Skill.CMD_EFFECT;
	}
	
	public int getVertex() {
		try {
			return Integer.valueOf(dic.objectForKey("vertex").toString());
		} catch (Exception e) {
			ErrorReporter.getInstance().addError("effect vertex does not exist");
			return 0;
		}
	}

	public void setVertex(int vertex) {
		dic.put("vertex", new NSNumber(vertex));
	}
}
