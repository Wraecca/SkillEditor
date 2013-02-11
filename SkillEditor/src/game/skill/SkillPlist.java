package game.skill;

import game.skill.ui.ErrorReporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.PropertyListParser;
import com.dd.plist.XMLPropertyListParser;

public class SkillPlist {

	public final static String PLIST_SKILL_DATA = "skillData.plist";
	public final static String PLIST_ROLE_ANIMATION = "roleAnimations.plist";
	public final static String PLIST_EFFECT_ANIMATION = "effectAnimations.plist";
	public final static String PLIST_EFFECT_SOUND = "effectSoundData.plist";

	public final static String[] PLIST_FILES = new String[] { PLIST_SKILL_DATA,
			PLIST_EFFECT_ANIMATION, PLIST_EFFECT_SOUND, PLIST_ROLE_ANIMATION };

	private NSDictionary roleAnimationNode;
	private NSDictionary effectAnimationNode;
	private NSDictionary effectSoundNode;
	private NSDictionary skillDataNode;

	private File file;
	private List<String> roles = new ArrayList<String>();
	private List<String> behaviors = new ArrayList<String>();

	public SkillPlist(String folder) {
		folder += System.getProperty("file.separator");
		this.file = new File(folder + PLIST_SKILL_DATA);
		try {
			skillDataNode = (NSDictionary) XMLPropertyListParser.parse(file);
			effectSoundNode = (NSDictionary) XMLPropertyListParser
					.parse(new File(folder + PLIST_EFFECT_SOUND));
			effectAnimationNode = (NSDictionary) XMLPropertyListParser
					.parse(new File(folder + PLIST_EFFECT_ANIMATION));
			roleAnimationNode = (NSDictionary) XMLPropertyListParser
					.parse(new File(folder + PLIST_ROLE_ANIMATION));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getSkills() {
		return skillDataNode.allKeys();
	}

	public List<String> getRoles() {
		if (roles.size() == 0) {
			String[] keys = roleAnimationNode.allKeys();
			for (String key : keys) {
				String role = key.split("_")[0];
				if (roles.contains(role) == false)
					roles.add(role);
			}
			Collections.sort(roles);
		}
		return roles;
	}

	public List<String> getSounds() {
		List<String> sounds = Arrays.asList(effectSoundNode.allKeys());
		Collections.sort(sounds);
		return sounds;
	}

	public List<String> getBehaviors(String roleName) {
		if (behaviors.size() == 0) {
			String[] keys = roleAnimationNode.allKeys();
			for (String key : keys) {
				String role = key.split("_")[1];
				if (behaviors.contains(role) == false)
					behaviors.add(role);
			}
			Collections.sort(behaviors);
		}
		return behaviors;
	}

	public List<String> getEffectAnimations() {
		List<String> animations = Arrays.asList(effectAnimationNode.allKeys());
		Collections.sort(animations);
		return animations;
	}

	public NSDictionary getEffectAnimation(String effectName) {
		return (NSDictionary) effectAnimationNode.objectForKey(effectName);
	}

	public void saveSkill(Skill skill) {

		try {
			skillDataNode = (NSDictionary) XMLPropertyListParser.parse(file);
			skillDataNode.put(skill.toString(), skill.getDictionary());
			PropertyListParser.saveAsXML(skillDataNode, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Skill getClonedSkill(String skillName) {

		try {
			NSDictionary sNode = (NSDictionary) XMLPropertyListParser
					.parse(file);
			Object obj = sNode.objectForKey(skillName);
			if (obj != null) {
				return new Skill(skillName, (NSDictionary) obj, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Skill createSkill(String newSkillName) {

		NSDictionary period = new NSDictionary();
		period.put("order", new NSNumber(0));
		period.put("duration", new NSNumber(3));

		NSDictionary dic = new NSDictionary();
		dic.put("period", period);

		return new Skill(newSkillName, dic, this);
	}

	public int getFrameCount(String role, String behavior) {
		if (role == null || role.equals("null")) {
			role = getRoles().get(0);
		}
		String key = role + "_" + behavior;
		NSDictionary dic = (NSDictionary) roleAnimationNode.objectForKey(key);
		if (dic == null) {
			ErrorReporter.getInstance().addError(key + " does not exist");
			return 1;
		}
		return Integer.valueOf(dic.objectForKey("frameCount").toString());
	}

	public static boolean allPlistsExist(String folder) {
		boolean exist = true;
		for (String fileName : SkillPlist.PLIST_FILES) {
			File file = new File(folder, fileName);
			if (file.exists() == false) {
				ErrorReporter.getInstance().addError(
						file.getAbsolutePath() + " does not exist");
				exist = false;
			}
		}
		return exist;
	}

}
