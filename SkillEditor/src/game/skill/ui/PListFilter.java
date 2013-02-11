package game.skill.ui;

import game.skill.SkillPlist;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PListFilter extends FileFilter{

	@Override
	public boolean accept(File f) {

		if (f.isDirectory())
			return true;

		if (f.getName().toLowerCase()
				.endsWith(SkillPlist.PLIST_SKILL_DATA.toLowerCase()))
			return true;

		return false;
	}

	@Override
	public String getDescription() {
		return "Only " + SkillPlist.PLIST_SKILL_DATA;
	}

}
