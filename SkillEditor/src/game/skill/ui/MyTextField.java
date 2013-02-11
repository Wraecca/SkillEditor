package game.skill.ui;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

public class MyTextField extends JFormattedTextField implements
		DocumentListener {
	
	public MyTextField() {
		super();
		setValue(0d);
		getDocument().addDocumentListener(this);
	}

	public MyTextField(NumberFormatter nf) {
		super(nf);
		setValue(0f);
		getDocument().addDocumentListener(this);
	}

	public void changedUpdate(DocumentEvent arg0) {
	}

	public void insertUpdate(DocumentEvent arg0) {

	}

	public void removeUpdate(DocumentEvent arg0) {
	}


}
