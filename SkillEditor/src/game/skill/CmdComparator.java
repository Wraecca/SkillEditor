package game.skill;


import java.util.Comparator;

public class CmdComparator implements Comparator<ICommand> {

	public int compare(ICommand c1, ICommand c2) {
		if (c1.getOrder() > c2.getOrder())
			return 1;
		else if (c1.getOrder() < c2.getOrder())
			return -1;
		else
			return 0;

	}
}