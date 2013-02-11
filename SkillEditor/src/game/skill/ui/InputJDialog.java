package game.skill.ui;

import game.skill.SkillPlist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class InputJDialog extends JDialog {
	
	public static final int Action_New = 0;
	public static final int Action_SaveAs = 1;
	
	private SkillPlist pList;
	private JTextField textField;
	
	public String skillName;
	private boolean result = false;
	
	public InputJDialog(Window owner) {
		super(owner);
		
		setModal(true);
		initiallize();
		setLocationRelativeTo(owner);
	}
	
	public boolean showJDialog(int action, SkillPlist pList){
		this.pList = pList;
		this.result = false;
		if (action == Action_New) {
			setTitle("開新Skill");
		}

		if (action == Action_SaveAs) {
			setTitle("另存Skill");
		}
		setVisible(true);
		return result;
	}

	private void initiallize() {
		setBounds(100, 100, 282, 142);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				skillName =textField.getText().trim();
				
				for (String sName : pList.getSkills()) {
					if (sName.equals(skillName)) {
						JOptionPane.showMessageDialog(InputJDialog.this,
								skillName + "已存在，請輸入其它名稱");
						return;
					}
				}

				result = true;
				setVisible(false);	
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				result = false;
				setVisible(false);
			}
		});
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		JLabel lblNewLabel = new JLabel("\u8ACB\u8F38\u5165Skill\u540D\u7A31");
		panel_1.add(lblNewLabel, "cell 0 0,aligny bottom");
		
		textField = new JTextField();
		panel_1.add(textField, "cell 0 1,growx,aligny top");
		textField.setColumns(10);
	}

}
