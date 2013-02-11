package game.skill;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;

public class Skill {

	public static final String CMD_ACTION = "action";
	public static final String CMD_MOVE = "move";
	public static final String CMD_EFFECT = "effect";
	public static final String CMD_CAMERA = "camera";
	public static final String CMD_SHOCK = "shock";
	public static final String CMD_HIT = "hit";
	public static final String CMD_SOUND = "sound";
	public static final String CMD_PERIOD = "period";

	private static final String[] KEYS = new String[] { CMD_ACTION, CMD_MOVE,
			CMD_EFFECT, CMD_CAMERA, CMD_SHOCK, CMD_HIT, CMD_SOUND, CMD_PERIOD};

	private String name;
	private NSDictionary dic;
	private SkillPlist pList;

	public Skill(String name, NSDictionary dic, SkillPlist pList) {
		this.name = name;
		this.dic = dic;
		this.pList = pList;
	}

	public void addCmd(ICommand cmd) {
		if(cmd instanceof CmdPeriod)
			return;
		NSArray array = (NSArray) dic.objectForKey(cmd.getCmdType());
		dic.put(cmd.getCmdType(), addArratItem(array, cmd.getNSObject()));
	}

	public NSDictionary getDictionary() {
		return dic;
	}

	public List<ICommand> getCmds() {
		
		List<ICommand> list = new ArrayList<ICommand>();
		
		for (String key : KEYS) {
			
			NSObject obj = dic.objectForKey(key);
			if (obj == null) {
				continue;
			}
			
			if (key.equals(CMD_PERIOD)) {
				list.add(new CmdPeriod((NSDictionary)obj));
				continue;
			}

			NSArray array = (NSArray) obj;
			for (NSObject arrayEle : array.getArray()) {
				NSDictionary dic = (NSDictionary) arrayEle;
				ICommand cmd = null;

				if (key.equals(CMD_ACTION))
					cmd = new CmdAction(dic, pList);
				else if (key.equals(CMD_MOVE))
					cmd = new CmdMove(dic);
				else if (key.equals(CMD_EFFECT))
					cmd = new CmdEffect(dic, pList);
				else if (key.equals(CMD_CAMERA))
					cmd = new CmdCamera(dic);
				else if (key.equals(CMD_SHOCK))
					cmd = new CmdShock(dic);
				else if (key.equals(CMD_HIT))
					cmd = new CmdHit(dic);
				else if (key.equals(CMD_SOUND))
					cmd = new CmdSound(dic);

				if (cmd != null) {
					list.add(cmd);
				}
			}
		}
		
		Collections.sort(list, new CmdComparator());
		return list;
	}
	
	public void removeCmd(ICommand cmd) {
		if (cmd == null || dic.objectForKey(cmd.getCmdType()) == null) {
			System.out.println("remove Cmd error");
			return;
		}
	
		NSArray array = (NSArray) dic.objectForKey(cmd.getCmdType());
		dic.put(cmd.getCmdType(), removeArrayItem(array, cmd.getNSObject()));
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private static NSArray removeArrayItem(NSArray array, NSObject object) {

		if (array.count() == 0)
			return new NSArray(0);
		
		List<NSObject> objs = new ArrayList<NSObject>();
		for (NSObject arrayObj : array.getArray()) {
			if (arrayObj.equals(object) == false) 
				objs.add(arrayObj);
		}
		
		NSArray newArray = new NSArray(objs.size());
		for (int i = 0; i < objs.size(); i++) {
			newArray.setValue(i, objs.get(i));
		}
		
		return newArray;
	}

	private static NSArray addArratItem(NSArray array, NSObject object) {
		int length = array == null ? 1 : array.getArray().length + 1;
		NSArray newArray = new NSArray(length);

		if(array!=null){
			for (int i = 0; i < array.count(); i++)
				newArray.setValue(i, array.objectAtIndex(i));
		}
		newArray.setValue(length-1, object);

		return newArray;
	}

}
