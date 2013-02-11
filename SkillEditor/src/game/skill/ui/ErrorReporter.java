package game.skill.ui;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ErrorReporter {

	private static ErrorReporter instance;

	private List<String> errors = new ArrayList<String>();
	private List<String> histoy = new ArrayList<String>();

	public static ErrorReporter getInstance() {
		if (instance == null)
			instance = new ErrorReporter();
		return instance;
	}

	public void addError(String error) {
		if (errors.contains(error) == false)
			errors.add(error);
	}

	public void reset() {
		histoy.addAll(errors);
		errors.clear();
	}

	public void show(Window window) {
		if (errors.size() == 0)
			return;

		String errs = "";
		for (String err : errors)
			errs += err + "\n";
		JOptionPane.showMessageDialog(window, errs, "¿ù»~³ø§i",
				JOptionPane.WARNING_MESSAGE);
		reset();
	}
}
