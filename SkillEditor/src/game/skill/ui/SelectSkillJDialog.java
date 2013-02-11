package game.skill.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class SelectSkillJDialog extends JDialog {

	private JPanel contentPanel = new JPanel();
	private JList list;
	
	private DefaultListModel listModel = new DefaultListModel();
	
	private boolean result;
	
	public String selectedSkillName;


	public SelectSkillJDialog(Window owner) {
		super(owner);
		
		setModal(true);
		setTitle("\u9078\u64C7Skill");
		initiallize();
		setLocationRelativeTo(owner);
	}

	private void initiallize() {
		setBounds(100, 100, 282, 220);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedSkillName = list.getSelectedValue().toString();
				result = true;
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = false;
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}
	
	public void setSkills(String[] skills){
		listModel.clear();
		if (skills == null)
			return;
		
		Arrays.sort(skills);
		for (String skill : skills)
			listModel.addElement(skill);
	}
	
	public boolean showDialog() {
		result = false;
	    setVisible(true);
	    return result;
	}

}
