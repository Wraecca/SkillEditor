package game.skill.ui;

import game.skill.CmdAction;
import game.skill.CmdCamera;
import game.skill.CmdEffect;
import game.skill.CmdHit;
import game.skill.CmdMove;
import game.skill.CmdPeriod;
import game.skill.CmdShock;
import game.skill.CmdSound;
import game.skill.ICommand;
import game.skill.IStartTime;
import game.skill.Skill;
import game.skill.SkillPlist;
import game.skill.bar.CmdInterval;
import game.skill.bar.CmdTimeBarModel;
import game.skill.bar.CmdTimeBarRenderer;
import game.skill.bar.CmdTimeBarRow;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import com.dd.plist.NSDictionary;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarViewerInterface;
import de.jaret.util.ui.timebars.mod.DefaultIntervalModificator;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarModelListener;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.model.TimeBarSelectionListener;
import de.jaret.util.ui.timebars.model.TimeBarSelectionModel;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainJFrame extends JFrame implements UncaughtExceptionHandler {

	private static final String SAVE_NOT = "(未存)";
	private static final String SAVE_YES = "(已存)";

	private static final String[] ARRAY_MOVE_DESTINATION = new String[] { 
		"原點", "對手前方", "對手後方", "自訂距離", "吹飛"};

	private static final String[] ARRAY_EFFECT_LOCATION = new String[] {
		"0_\u5DF1\u65B9\u4E2D\u592E", 
		"1_\u5DF1\u65B9\u524D\u65B9", 
		"2_\u5DF1\u65B9\u8173\u5E95", 
		"3_\u5DF1\u65B9\u5F8C\u65B9", 
		"4_\u5DF1\u65B9\u5929\u7A7A", 
		"10_\u5C0D\u624B\u4E2D\u592E", 
		"11_\u5C0D\u624B\u524D\u65B9", 
		"12_\u5C0D\u624B\u8173\u5E95", 
		"13_\u5C0D\u624B\u5F8C\u65B9\uFF08\u756B\u9762\u5916\uFF09", 
		"14_\u5C0D\u624B\u5929\u7A7A", 
		"20_\u6230\u5834\u6B63\u4E2D\u592E", 
		"21_\u6230\u5834\u4E0A\u65B9"};

	private static final String[] ARRAY_IS_SELF = new String[] {
		"己方", "對方"};
	
	private JPanel contentPane;
	private MyTextField actionDelayTextField;
	private MyTextField actionStartTimeTextField;
	private MyTextField moveStartTimeTextField;
	private MyTextField moveDurationTextField;
	private MyTextField effectDurationTextField;
	private MyTextField effectStartTimeTextField;
	private MyTextField hitStartTimeTextField;
	private MyTextField hitFontSizeTextField;
	private MyTextField hitTimesTextField;
	private MyTextField hitIntervalTextField;
	private MyTextField cameraZoomTextField;
	private MyTextField cameraDurationTextField;
	private MyTextField cameraStartTimeTextField;
	private MyTextField shockStartTimeTextField;
	private MyTextField shockDurationTextField;
	private MyTextField shockLevelTextField;
	private MyTextField periodDurationTextField;
	private MyTextField soundStratTimeTextField;
	private MyTextField moveCustomTextField;
	
	private JButton saveSkillAsButton;
	private JButton btnNewButton_7;
	private JButton saveSkillButton;
	private JButton btnNewButton_1;
	private JButton modifyCmdButton;
	private JButton newCmdButton;
	
	private JFileChooser fileChooser = new JFileChooser();
	private SelectSkillJDialog selectSkillDialog = new SelectSkillJDialog(this);
	private InputJDialog inputDialog = new InputJDialog(this);
	
	private JComboBox actionIsSelfComboBox;
	private JComboBox actionReferenceRoleComboBox;
	private JComboBox effectAnimationcomboBox;
	private JComboBox actionBehaviorComboBox;
	private JComboBox moveIsSelfComboBox;
	private JComboBox moveDestinationComboBox;
	private JComboBox cameraTargetComboBox;
	private JComboBox hitIsSelfComboBox;
	private JComboBox effectStartPointcomboBox;
	private JComboBox effectEndPointComboBox;
	private JComboBox soundFileNamecomboBox;
	
	private JLabel effectDurationLabel;
	private JLabel moveCustomLabel;
	
	private JTabbedPane tabbedPane;
	private JCheckBox effectIsRepeatCheckBox;
	private JSlider slider;

	private boolean isCtrlPressed = false;
	private boolean isAltPressed = false;
	private boolean isOpening = false;
	private boolean isUpdatingAction = false;
	
	private SkillPlist skillPlist;
	private Skill editingSkill;
	
	private TimeBarViewer timeBarViwer;
	private CmdTimeBarRow selectedRow;
	private CmdTimeBarModel rowBarModel;

	private DecimalFormat df = new DecimalFormat("##.###");
	private NumberFormatter nf = new NumberFormatter(df);
	private MyTextField moveVertexTextField;
	private MyTextField effectVertexTextField;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrame frame = new MainJFrame();
					frame.setVisible(true);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public MainJFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				checkAndOpenFiles();
			}
		});
		Thread.setDefaultUncaughtExceptionHandler(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainJFrame.class.getResource("/game/skill/img/logol.png")));
		initiallize();
		fileChooser.setFileFilter(new PListFilter());
		moveIsSelfComboBox.setSelectedIndex(1);
		moveIsSelfComboBox.setSelectedIndex(0);
		

	}
	
	private void checkAndOpenFiles(){
		String folder = new File(".").getAbsoluteFile().getParent();
		boolean filesExist = SkillPlist.allPlistsExist(folder);
		if (filesExist) {
			openPList(folder);
		} else {
			ErrorReporter.getInstance().show(this);
		}
	}

	private void initiallize() {
		setFocusTraversalKeysEnabled(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("\u958B\u555FPList");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openPList();
			}
		});
		toolBar.add(btnNewButton);
		
		btnNewButton_7 = new JButton("\u65B0\u589ESkill");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newSkill();
			}
		});
		btnNewButton_7.setEnabled(false);
		toolBar.add(btnNewButton_7);
		
		btnNewButton_1 = new JButton("\u958B\u555Fskill");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSkill();
			}
		});
		toolBar.add(btnNewButton_1);
		
		saveSkillButton = new JButton("\u5132\u5B58skill");
		saveSkillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveSkill();
			}
		});
		saveSkillButton.setEnabled(false);
		toolBar.add(saveSkillButton);
		
		saveSkillAsButton = new JButton("\u53E6\u5B58skill");
		saveSkillAsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSkillAs();
			}
		});
		saveSkillAsButton.setEnabled(false);
		toolBar.add(saveSkillAsButton);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][]", "[][][grow]"));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent arg0) {
//				if (newCmdButton == null || editingSkill == null)
//					return;
//				String tabName =tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
//				newCmdButton.setEnabled(!tabName.equals(Skill.CMD_PERIOD));
//				
//				if (selectedRow == null)
//					return;
//				modifyCmdButton.setEnabled(tabName.equals(selectedRow.getCmd().getCmdType()));
//			}
//		});
		panel.add(tabbedPane, "cell 0 0,grow");
		
		JPanel actionPanel = new JPanel();
		tabbedPane.addTab(Skill.CMD_ACTION, null, actionPanel, null);
		actionPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));
		
		JLabel lblNewLabel = new JLabel("\u5C0D\u8C61");
		actionPanel.add(lblNewLabel, "cell 0 0,growx");
		
		actionIsSelfComboBox = new JComboBox();
		actionIsSelfComboBox.setModel(new DefaultComboBoxModel(new String[] {"\u5DF1\u65B9", "\u5C0D\u65B9"}));
		actionPanel.add(actionIsSelfComboBox, "cell 1 0,growx");
		 
		JLabel lblNewLabel_1 = new JLabel("\u52D5\u756B");
		actionPanel.add(lblNewLabel_1, "cell 0 1,growx");
		
		actionBehaviorComboBox = new JComboBox(new DefaultComboBoxModel());
		actionPanel.add(actionBehaviorComboBox, "cell 1 1,growx");
		
		JLabel lblNewLabel_2 = new JLabel("\u555F\u59CB\u6642\u9593");
		actionPanel.add(lblNewLabel_2, "cell 0 2,growx");
		
		actionStartTimeTextField = new MyTextField(nf);
		actionPanel.add(actionStartTimeTextField, "cell 1 2,growx");
		actionStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("\u64AD\u653E\u901F\u5EA6");
		actionPanel.add(lblNewLabel_3, "cell 0 3,growx");
		
		actionDelayTextField = new MyTextField(nf);
		actionPanel.add(actionDelayTextField, "cell 1 3,growx");
		actionDelayTextField.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("\u53C3\u8003\u8173\u8272");
		actionPanel.add(lblNewLabel_4, "cell 0 4,growx");
		
		actionReferenceRoleComboBox = new JComboBox();
		actionReferenceRoleComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				actionBehaviorComboBox.removeAllItems();
				for (String behavior : skillPlist.getBehaviors(e.getItem().toString()))
					actionBehaviorComboBox.addItem(behavior);
			}
		});
		actionReferenceRoleComboBox.setModel(new DefaultComboBoxModel(new String[] {}));
		actionPanel.add(actionReferenceRoleComboBox, "cell 1 4,growx");
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab(Skill.CMD_MOVE, null, panel_2, null);
		panel_2.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));
		
		JLabel lblNewLabel_5 = new JLabel("\u5C0D\u8C61");
		panel_2.add(lblNewLabel_5, "cell 0 0,growx");
		
		moveIsSelfComboBox = new JComboBox();
		moveIsSelfComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getItem().equals(ARRAY_IS_SELF[1]) && 
				   moveDestinationComboBox.getItemAt(4)==null){
					moveDestinationComboBox.addItem(ARRAY_MOVE_DESTINATION[4]);
				}
				else{
					if(moveDestinationComboBox.getSelectedIndex()==4)
						moveDestinationComboBox.setSelectedIndex(3);
					moveDestinationComboBox.removeItem(ARRAY_MOVE_DESTINATION[4]);
				}
				moveDestinationComboBox.invalidate();
			}
		});
		moveIsSelfComboBox.setModel(new DefaultComboBoxModel(ARRAY_IS_SELF));
		panel_2.add(moveIsSelfComboBox, "cell 1 0,growx");
		
		JLabel lblNewLabel_6 = new JLabel("\u76EE\u7684\u5730");
		panel_2.add(lblNewLabel_6, "cell 0 1,growx");
		
		moveDestinationComboBox = new JComboBox();
		moveDestinationComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				boolean isEnable = moveDestinationComboBox.getSelectedIndex() == 3 ? true:false;
				moveCustomLabel.setEnabled(isEnable);
				moveCustomTextField.setEnabled(isEnable);
			}
		});
		moveDestinationComboBox.setModel(new DefaultComboBoxModel(ARRAY_MOVE_DESTINATION));
		panel_2.add(moveDestinationComboBox, "cell 1 1,growx");
		
		moveCustomLabel = new JLabel("\u81EA\u8A02\u8DDD\u96E2");
		panel_2.add(moveCustomLabel, "cell 0 2,alignx left");
		
		moveCustomTextField = new MyTextField(nf);
		panel_2.add(moveCustomTextField, "cell 1 2,growx");
		moveCustomTextField.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("\u8D77\u59CB\u6642\u9593");
		panel_2.add(lblNewLabel_7, "cell 0 3,growx");
		
		moveStartTimeTextField = new MyTextField(nf);
		panel_2.add(moveStartTimeTextField, "cell 1 3,growx");
		moveStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("\u6301\u7E8C\u6642\u9593");
		panel_2.add(lblNewLabel_8, "cell 0 4,growx");
		
		moveDurationTextField = new MyTextField(nf);
		panel_2.add(moveDurationTextField, "cell 1 4,growx");
		moveDurationTextField.setColumns(10);
		
		JLabel lblNewLabel_28 = new JLabel("\u62CB\u7269\u7DDA\u9802\u9EDE");
		panel_2.add(lblNewLabel_28, "cell 0 5,alignx trailing");
		
		moveVertexTextField = new MyTextField();
		moveVertexTextField.setColumns(10);
		panel_2.add(moveVertexTextField, "cell 1 5,growx");
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab(Skill.CMD_EFFECT, null, panel_3, null);
		panel_3.setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));
		
		JLabel lblNewLabel_9 = new JLabel("\u52D5\u756B");
		panel_3.add(lblNewLabel_9, "cell 0 0,growx");
		
		effectAnimationcomboBox = new JComboBox();
		panel_3.add(effectAnimationcomboBox, "cell 1 0,growx");
		
		JLabel lblNewLabel_10 = new JLabel("\u8D77\u59CB\u6642\u9593");
		panel_3.add(lblNewLabel_10, "cell 0 1,growx");
		
		effectStartTimeTextField = new MyTextField(nf);
		panel_3.add(effectStartTimeTextField, "cell 1 1,growx");
		effectStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_11 = new JLabel("\u8D77\u59CB\u9EDE");
		panel_3.add(lblNewLabel_11, "cell 0 2,growx");
		
		effectStartPointcomboBox = new JComboBox();
		effectStartPointcomboBox.setModel(new DefaultComboBoxModel(ARRAY_EFFECT_LOCATION));
		panel_3.add(effectStartPointcomboBox, "cell 1 2,growx");
		
		JLabel lblNewLabel_12 = new JLabel("\u7D42\u9EDE");
		panel_3.add(lblNewLabel_12, "cell 0 3,growx");
		
		effectEndPointComboBox = new JComboBox();
		effectEndPointComboBox.setModel(new DefaultComboBoxModel(ARRAY_EFFECT_LOCATION));
		panel_3.add(effectEndPointComboBox, "cell 1 3,growx");
		
		effectIsRepeatCheckBox = new JCheckBox("\u662F\u5426\u5FAA\u74B0\u64AD\u653E");
		effectIsRepeatCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				effectDurationLabel.setEnabled(effectIsRepeatCheckBox.isSelected());
				effectDurationTextField.setEnabled(effectIsRepeatCheckBox.isSelected());
			}
		});
		panel_3.add(effectIsRepeatCheckBox, "cell 0 4 2 1");
		
		effectDurationLabel = new JLabel("\u6301\u7E8C\u6642\u9593");
		panel_3.add(effectDurationLabel, "cell 0 5,growx");
		
		effectDurationTextField = new MyTextField(nf);
		panel_3.add(effectDurationTextField, "cell 1 5,growx");
		effectDurationTextField.setColumns(10);
		
		JLabel lblNewLabel_30 = new JLabel("\u62CB\u7269\u7DDA\u9802\u9EDE");
		panel_3.add(lblNewLabel_30, "cell 0 6,alignx trailing");
		
		effectVertexTextField = new MyTextField();
		effectVertexTextField.setColumns(10);
		panel_3.add(effectVertexTextField, "cell 1 6,growx");
		
		JPanel panel_7 = new JPanel();
		tabbedPane.addTab(Skill.CMD_CAMERA, null, panel_7, null);
		panel_7.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		
		JLabel lblNewLabel_21 = new JLabel("\u5C0D\u8C61");
		panel_7.add(lblNewLabel_21, "cell 0 0,growx");
		
		cameraTargetComboBox = new JComboBox();
		cameraTargetComboBox.setModel(new DefaultComboBoxModel(new String[] {"\u5DF2\u65B9", "\u5C0D\u65B9", "\u756B\u9762\u4E2D\u592E"}));
		panel_7.add(cameraTargetComboBox, "cell 1 0,growx");
		
		JLabel lblNewLabel_22 = new JLabel("\u8D77\u59CB\u6642\u9593");
		panel_7.add(lblNewLabel_22, "cell 0 1,growx");
		
		cameraStartTimeTextField = new MyTextField(nf);
		panel_7.add(cameraStartTimeTextField, "cell 1 1,growx");
		cameraStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_23 = new JLabel("\u6301\u7E8C\u6642\u9593");
		panel_7.add(lblNewLabel_23, "cell 0 2,growx");
		
		cameraDurationTextField = new MyTextField(nf);
		panel_7.add(cameraDurationTextField, "cell 1 2,growx");
		cameraDurationTextField.setColumns(10);
		
		JLabel lblNewLabel_24 = new JLabel("\u8B8A\u7126");
		panel_7.add(lblNewLabel_24, "cell 0 3,growx");
		
		cameraZoomTextField = new MyTextField();
		panel_7.add(cameraZoomTextField, "cell 1 3,growx");
		cameraZoomTextField.setColumns(10);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab(Skill.CMD_SHOCK, null, panel_8, null);
		panel_8.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblNewLabel_25 = new JLabel("\u555F\u59CB\u6642\u9593");
		panel_8.add(lblNewLabel_25, "cell 0 0,growx");
		
		shockStartTimeTextField = new MyTextField(nf);
		panel_8.add(shockStartTimeTextField, "cell 1 0,growx");
		shockStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_26 = new JLabel("\u6301\u7E8C\u6642\u9593");
		panel_8.add(lblNewLabel_26, "cell 0 1,growx");
		
		shockDurationTextField = new MyTextField(nf);
		panel_8.add(shockDurationTextField, "cell 1 1,growx");
		shockDurationTextField.setColumns(10);
		
		JLabel lblNewLabel_27 = new JLabel("\u7B49\u7D1A");
		panel_8.add(lblNewLabel_27, "cell 0 2,growx");
		
		shockLevelTextField = new MyTextField();
		panel_8.add(shockLevelTextField, "cell 1 2,growx");
		shockLevelTextField.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab(Skill.CMD_HIT, null, panel_5, null);
		panel_5.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		
		JLabel lblNewLabel_16 = new JLabel("\u5C0D\u8C61");
		panel_5.add(lblNewLabel_16, "cell 0 0,growx");
		
		hitIsSelfComboBox = new JComboBox();
		hitIsSelfComboBox.setModel(new DefaultComboBoxModel(ARRAY_IS_SELF));
		panel_5.add(hitIsSelfComboBox, "cell 1 0,growx");
		
		JLabel lblNewLabel_17 = new JLabel("\u8D77\u59CB\u6642\u9593");
		panel_5.add(lblNewLabel_17, "cell 0 1,growx");
		
		hitStartTimeTextField = new MyTextField(nf);
		panel_5.add(hitStartTimeTextField, "cell 1 1,growx");
		hitStartTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_18 = new JLabel("\u5B57\u9AD4\u5927\u5C0F");
		panel_5.add(lblNewLabel_18, "cell 0 2,growx");
		
		hitFontSizeTextField = new MyTextField(nf);
		panel_5.add(hitFontSizeTextField, "cell 1 2,growx");
		hitFontSizeTextField.setColumns(10);
		
		JLabel lblNewLabel_19 = new JLabel("\u986F\u793A\u6B21\u6578");
		panel_5.add(lblNewLabel_19, "cell 0 3,growx");
		
		hitTimesTextField = new MyTextField(nf);
		panel_5.add(hitTimesTextField, "cell 1 3,growx");
		hitTimesTextField.setColumns(10);
		
		JLabel lblNewLabel_20 = new JLabel("\u986F\u793A\u9593\u9694");
		panel_5.add(lblNewLabel_20, "cell 0 4,growx");
		
		hitIntervalTextField = new MyTextField(nf);
		panel_5.add(hitIntervalTextField, "cell 1 4,growx");
		hitIntervalTextField.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab(Skill.CMD_SOUND, null, panel_6, null);
		panel_6.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblNewLabel_14 = new JLabel("\u8D77\u59CB\u6642\u9593");
		panel_6.add(lblNewLabel_14, "cell 0 0,alignx trailing");
		
		soundStratTimeTextField = new MyTextField(nf);
		panel_6.add(soundStratTimeTextField, "cell 1 0,growx");
		soundStratTimeTextField.setColumns(10);
		
		JLabel lblNewLabel_15 = new JLabel("\u97F3\u6548");
		panel_6.add(lblNewLabel_15, "cell 0 1,growx");
		
		soundFileNamecomboBox = new JComboBox();
		panel_6.add(soundFileNamecomboBox, "cell 1 1,growx");
		
		JButton btnNewButton_4 = new JButton("\u8A66\u807D");
		panel_6.add(btnNewButton_4, "cell 1 2,alignx right");
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("period", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		JLabel lblNewLabel_13 = new JLabel("duration");
		panel_1.add(lblNewLabel_13, "cell 0 0,alignx trailing");
		
		periodDurationTextField = new MyTextField(nf);
		panel_1.add(periodDurationTextField, "cell 1 0,growx");
		periodDurationTextField.setColumns(10);
		
		JPanel panel_10 = new JPanel();
		panel.add(panel_10, "cell 1 0,grow");
		panel_10.setLayout(new MigLayout("", "[][]", "[][][][][]"));
		
		newCmdButton = new JButton("\u65B0\u589E");
		newCmdButton.setEnabled(false);
		panel_10.add(newCmdButton, "cell 0 2");
		newCmdButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newCmd(-1);
			}
		});
		
		modifyCmdButton = new JButton("\u4FEE\u6539");
		modifyCmdButton.setEnabled(false);
		modifyCmdButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modifyCmd();
			}
		});
		panel_10.add(modifyCmdButton, "cell 0 3");
		
		JPanel panel_9 = new JPanel();
		panel.add(panel_9, "cell 0 1 2 1,grow");
		panel_9.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		JLabel lblNewLabel_29 = new JLabel("\u6642\u9593\u523B\u5EA6");
		panel_9.add(lblNewLabel_29);
		
		slider = createScaleSlider();
		panel_9.add(slider);
		
		timeBarViwer = createTimeBarViewer();
        panel.add(timeBarViwer, "cell 0 2 2 1,grow");
	}
	
	private TimeBarViewer createTimeBarViewer(){
		rowBarModel = new CmdTimeBarModel();
		rowBarModel.addRow(new CmdTimeBarRow(new CmdPeriod(new NSDictionary())));
		
		TimeBarViewer timeBarViwer = new TimeBarViewer(rowBarModel);
        timeBarViwer.getSelectionModel().setMultipleSelectionAllowed(false);
        timeBarViwer.setDrawRowGrid(true);
        timeBarViwer.setDrawOverlapping(false);
        timeBarViwer.getSelectionModel().setRowSelectionAllowed(false);
        timeBarViwer.setTimeScalePosition(0);
        timeBarViwer.setRowHeight(15);
        timeBarViwer.setTimeBarRenderer(new CmdTimeBarRenderer());
        timeBarViwer.setTimeScalePosition(TimeBarViewerInterface.TIMESCALE_POSITION_TOP);
        timeBarViwer.addKeyListener(new MyListener());
        timeBarViwer.getSelectionModel().addTimeBarSelectionListener(new MyListener());
        timeBarViwer.getModel().addTimeBarModelListener(new MyListener());
        timeBarViwer.setAutoscrollEnabled(false);
        
//      timeBarViwer.setAutoscrollEnabled(false);
//      timeBarViwer.setPixelPerSecond(0.1);
//      timeBarViwer.setMinDate(new JaretDate());
//      timeBarViwer.setSelectionDelta(6);
//		timeBarViwer.setStartDate(new JaretDate(2000,1,1,1,1,1));
//      timeBarViwer.setSecondsDisplayed(3, false);
        
		DefaultIntervalModificator modificator = new DefaultIntervalModificator(){
			
			public boolean shiftAllowed(TimeBarRow row, Interval interval, JaretDate newBegin) {
				if (newBegin.getMinutes() != 0)
					return false;
				else
					return true;
			}

			public boolean isShiftingAllowed(TimeBarRow row, Interval interval) {
				if (isAltPressed == false)
					return false;
				if ((interval instanceof CmdInterval) == false)
					return false;
				if (((CmdInterval) interval).getCmd() instanceof CmdPeriod)
					return false;
				return true;
			}

			
			public boolean isSizingAllowed(TimeBarRow row, Interval interval) {
				return false;
			}
			
		};
		timeBarViwer.addIntervalModificator(modificator);

		return timeBarViwer;
	}
	
	private JSlider createScaleSlider(){

        Hashtable<Integer, JLabel> dic = new Hashtable<Integer, JLabel>();
        dic.put(new Integer(1), new JLabel("1s"));
        dic.put(new Integer(3), new JLabel("3s"));
        dic.put(new Integer(5), new JLabel("5s"));
        
        final JSlider slider = new JSlider(1,5);
        slider.setLabelTable(dic);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
				int x = (int) slider.getValue();
				timeBarViwer.setSecondsDisplayed( x, false);
            }
        });
        return slider;
	}

	private void copyCmd() {
		if (selectedRow == null)
			return;
		
		skillPlist.getEffectAnimations();
		writeDataToUI(selectedRow.getCmd());
		newCmd(-1);
	}
	
	private void modifyCmd(){
		
		if (selectedRow == null)
			return;
		
		String tabName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		int index = rowBarModel.getIndexForRow(selectedRow);
		ICommand cmd = selectedRow.getCmd();
		
		if (tabName.equals(cmd.getCmdType()) == true) {
			readDataToCmd(cmd);
			isUpdatingAction = true;
			selectedRow.recalculateTime();
			
			// set referene role to all action
			if(tabName.equals(Skill.CMD_ACTION)){
				for (int i = 0; i < rowBarModel.getRowCount(); i++) {
					CmdTimeBarRow row = (CmdTimeBarRow) rowBarModel.getRow(i);
					if ((row.getCmd() instanceof CmdAction) == false || row == selectedRow)
						continue;
					CmdAction act = (CmdAction) row.getCmd();
					act.setReferenceRole(actionReferenceRoleComboBox.getSelectedItem().toString());
					row.recalculateTime();
				}
			}
			isUpdatingAction = false;
			
		}
		
		else{
			if(cmd instanceof CmdPeriod){
				JOptionPane.showMessageDialog(this, 
						"無法修改period為其他類別","警告",JOptionPane.WARNING_MESSAGE);
				return;
			}
			editingSkill.removeCmd(cmd);
			rowBarModel.remRow(rowBarModel.getRow(index));
			newCmd(index);
		}
		
		setTitle(editingSkill.toString() + SAVE_NOT);
	}
	
	private void moveCmd(double d) {
		if (selectedRow == null)
			return;
		
		CmdInterval interval = (CmdInterval) selectedRow.getIntervals().get(0);
		long begin = interval.getBegin().getSeconds()*1000+interval.getBegin().getMillis();
		
		if (d * 1000 + begin < 0)
			return;
		
		interval.getBegin().advanceMillis((long) (d*1000));
		interval.getEnd().advanceMillis((long) (d*1000));
	}

	private void newCmd(int index){
		
		String tabName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		ICommand cmd = null;
		
		if (tabName.equals(Skill.CMD_ACTION))
			cmd = new CmdAction(new NSDictionary(), skillPlist);
		else if (tabName.equals(Skill.CMD_MOVE))
			cmd = new CmdMove(new NSDictionary());
		else if (tabName.equals(Skill.CMD_EFFECT))
			cmd = new CmdEffect(new NSDictionary(), skillPlist);
		else if (tabName.equals(Skill.CMD_CAMERA))
			cmd = new CmdCamera(new NSDictionary());
		else if (tabName.equals(Skill.CMD_SHOCK))
			cmd = new CmdShock(new NSDictionary());
		else if (tabName.equals(Skill.CMD_HIT))
			cmd = new CmdHit(new NSDictionary());
		else if (tabName.equals(Skill.CMD_SOUND))
			cmd = new CmdSound(new NSDictionary());
	
		if (cmd == null)
			return;
		
		setTitle(editingSkill.toString()+SAVE_NOT);
		readDataToCmd(cmd);
		editingSkill.addCmd(cmd);
		if (index < 0 || index > rowBarModel.getRowCount())
			rowBarModel.addRow(new CmdTimeBarRow(cmd));
		else
			rowBarModel.addRow(index, new CmdTimeBarRow(cmd));
	}

	private void newSkill(){
		if (inputDialog.showJDialog(InputJDialog.Action_New, skillPlist)) {
			editingSkill = skillPlist.createSkill(inputDialog.skillName);
			startToEdit();
		}
	}
	
	private void openSkill(){
		selectSkillDialog.setSkills(skillPlist.getSkills());
		if (selectSkillDialog.showDialog() == true) {
			String skillName = selectSkillDialog.selectedSkillName;
			editingSkill = skillPlist.getClonedSkill(skillName);
			startToEdit();
		}
		ErrorReporter.getInstance().show(this);
	}

	private void openPList() {
		int returnVal = fileChooser.showOpenDialog(MainJFrame.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String folder = fileChooser.getSelectedFile().getParent();
			if (SkillPlist.allPlistsExist(folder))
				openPList(folder);
			else
				ErrorReporter.getInstance().show(this);
		}
	}
	
	private void openPList(String folder){
		
		skillPlist = new SkillPlist(folder);
		rowBarModel.removeAllRow();

		actionReferenceRoleComboBox.removeAllItems();
		for (String behavior : skillPlist.getRoles())
			actionReferenceRoleComboBox.addItem(behavior);

		effectAnimationcomboBox.removeAllItems();
		for (String anim : skillPlist.getEffectAnimations())
			effectAnimationcomboBox.addItem(anim);

		soundFileNamecomboBox.removeAllItems();
		for (String anim : skillPlist.getSounds())
			soundFileNamecomboBox.addItem(anim);
		
		btnNewButton_1.setEnabled(true);
		btnNewButton_7.setEnabled(true);
		saveSkillButton.setEnabled(false);
		saveSkillAsButton.setEnabled(false);
		newCmdButton.setEnabled(false);
		modifyCmdButton.setEnabled(false);
		setTitle("");
	}

	private void readDataToCmd(ICommand cmd){

		if(cmd instanceof CmdAction){
			CmdAction sAction = (CmdAction) cmd;
			sAction.setSelf(actionIsSelfComboBox.getSelectedIndex()==0?true:false);
			sAction.setStartTime(Double.valueOf(actionStartTimeTextField.getText()));
			sAction.setBehavior(actionBehaviorComboBox.getSelectedItem().toString());
			sAction.setDelay(Double.valueOf(actionDelayTextField.getText()));
			sAction.setReferenceRole(actionReferenceRoleComboBox.getSelectedItem().toString());
		}
		
		else if(cmd instanceof CmdMove){
			CmdMove sMove = (CmdMove) cmd;
			sMove.setSelf(moveIsSelfComboBox.getSelectedIndex()==0?true:false);
			sMove.setStartTime(Double.valueOf(moveStartTimeTextField.getText()));
			sMove.setDuration(Double.valueOf(moveDurationTextField.getText()));
			sMove.setDestination(moveDestinationComboBox.getSelectedIndex(), 
					Double.valueOf(moveCustomTextField.getText()));
			sMove.setVertex(Integer.valueOf(moveVertexTextField.getText()));
		}
		
		else if(cmd instanceof CmdEffect){
			CmdEffect sEffect = (CmdEffect) cmd;
			sEffect.setStartTime(Double.valueOf(effectStartTimeTextField.getText()));
			sEffect.setAnimation(effectAnimationcomboBox.getSelectedItem().toString());
			sEffect.setDuration(effectIsRepeatCheckBox.isSelected()?Double.valueOf(effectDurationTextField.getText()):0);
			sEffect.setStartPoint(getLocationIndex(effectStartPointcomboBox.getSelectedItem().toString()));
			sEffect.setEndPoint(getLocationIndex(effectEndPointComboBox.getSelectedItem().toString()));
			sEffect.setVertex(Integer.valueOf(effectVertexTextField.getText()));
		}
		
		else if(cmd instanceof CmdCamera){
			CmdCamera sCamera = (CmdCamera) cmd;
			sCamera.setTarget(cameraTargetComboBox.getSelectedIndex());
			sCamera.setStartTime(Double.valueOf(cameraStartTimeTextField.getText()));
			sCamera.setDuration(Double.valueOf(cameraDurationTextField.getText()));
			sCamera.setZoom(Double.valueOf(cameraZoomTextField.getText()));
		}
		
		else if(cmd instanceof CmdShock){
			CmdShock sShock = (CmdShock) cmd;
			sShock.setStartTime(Double.valueOf(shockStartTimeTextField.getText()));
			sShock.setDuration(Double.valueOf(shockDurationTextField.getText()));
			sShock.setLevel(Integer.valueOf(shockLevelTextField.getText()));
		}
		
		else if(cmd instanceof CmdHit){
			CmdHit sHit = (CmdHit) cmd;
			sHit.setSelf(hitIsSelfComboBox.getSelectedIndex()==0?true:false);
			sHit.setStartTime(Double.valueOf(hitStartTimeTextField.getText()));
			sHit.setFontSize(Integer.valueOf(hitFontSizeTextField.getText()));
			sHit.setTimes(Integer.valueOf(hitTimesTextField.getText()));
			sHit.setInterval(Double.valueOf(hitIntervalTextField.getText()));
		}
		
		else if(cmd instanceof CmdSound){
			CmdSound cSound = (CmdSound) cmd;
			cSound.setStartTime(Double.valueOf(soundStratTimeTextField.getText()));
			cSound.setSoundFileName(soundFileNamecomboBox.getSelectedItem().toString());
		}
		
		else if(cmd instanceof CmdPeriod){
			CmdPeriod cPeriod = (CmdPeriod) cmd;
			cPeriod.setDuration(Double.valueOf(periodDurationTextField.getText()));
		}
	}

	private void removeCmd(){
		
		if (selectedRow == null)
			return;
		
		ICommand cmd = selectedRow.getCmd();
		if(cmd.getCmdType().equals(Skill.CMD_PERIOD) == true)
			return;
		
		int result = JOptionPane.showConfirmDialog(this, "確認刪除此筆資料?", "警告",
				JOptionPane.YES_NO_OPTION);

		if (result != JOptionPane.YES_OPTION)
			return;
	
		int index = rowBarModel.getIndexForRow(selectedRow);
		editingSkill.removeCmd(cmd);
		rowBarModel.remRow(rowBarModel.getRow(index));
		setTitle(editingSkill.toString() + SAVE_NOT);
		
		index++;
		if (index > rowBarModel.getRowCount() - 1)
			index = rowBarModel.getRowCount();
		selectInterval(index);
		timeBarViwer.setLastRow(selectedRow);
	}

	private void startToEdit(){
		
		isOpening = true;
		
		saveSkillButton.setEnabled(true);
		saveSkillAsButton.setEnabled(true);
		
		newCmdButton.setEnabled(true);
		modifyCmdButton.setEnabled(false);
		
		setTitle(editingSkill.toString() + SAVE_NOT);
		rowBarModel.removeAllRow();
		for(ICommand cmd : editingSkill.getCmds()){
			CmdTimeBarRow row = new CmdTimeBarRow(cmd);
			rowBarModel.addRow(row);
		}
		
		int x = (int) slider.getValue();
		timeBarViwer.setSecondsDisplayed( x, false);
		
		isOpening = false;		
	}

	private void switchTabbedPanel(String tabName) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).equals(tabName)) {
				tabbedPane.setSelectedIndex(i);
				return;
			}
		}
	}

	private void saveSkillAs() {
		if (inputDialog.showJDialog(InputJDialog.Action_SaveAs, skillPlist)) {
			editingSkill.setName(inputDialog.skillName);
			saveSkill();
		}
	}

	private void selectInterval(int delta){
		if (selectedRow == null)
			return;
		
		int index = rowBarModel.getIndexForRow(selectedRow) + delta;
		if (index >= 0 && index < rowBarModel.getRowCount()) {
			TimeBarRow raw = rowBarModel.getRow(index);
			timeBarViwer.getSelectionModel().setSelectedInterval(raw.getIntervals().get(0));
		}
	}

	private void saveSkill() {
		rowBarModel.saveOrder();
		skillPlist.saveSkill(editingSkill);
		setTitle(editingSkill.toString()+SAVE_YES);
	}

	private void writeDataToUI(ICommand sTime){

		if(sTime instanceof CmdAction){
			CmdAction cAction = (CmdAction) sTime;
			actionIsSelfComboBox.setSelectedIndex(cAction.isSelf() == true ? 0 : 1);
			actionStartTimeTextField.setValue(cAction.getStartTime());
			actionBehaviorComboBox.setSelectedItem(cAction.getBehavior());
			actionDelayTextField.setValue(cAction.getDelay());
			actionReferenceRoleComboBox.setSelectedItem(cAction.getRefereneRole());
		}
		
		else if(sTime instanceof CmdMove){
			CmdMove cMove = (CmdMove) sTime;			
			moveIsSelfComboBox.setSelectedIndex(cMove.isSelf() == true ? 0 : 1);
			moveDestinationComboBox.setSelectedItem(ARRAY_MOVE_DESTINATION[cMove.getDestination()]);
			moveStartTimeTextField.setValue(cMove.getStartTime());
			moveDurationTextField.setValue(cMove.getDuration());
			moveCustomTextField.setValue(cMove.getCustomMoveRange());
			moveVertexTextField.setValue(cMove.getVertex());
			
			boolean isEnable = cMove.getDestination() == 3 ? true : false;
			moveCustomLabel.setEnabled(isEnable);
			moveCustomTextField.setEnabled(isEnable);
		}
		
		else if(sTime instanceof CmdEffect){
			CmdEffect cEffect = (CmdEffect) sTime;
			effectStartTimeTextField.setValue(cEffect.getStartTime());
			effectVertexTextField.setValue(cEffect.getVertex());
			
			effectStartPointcomboBox.setSelectedItem(getLocationString(cEffect.getStartPoint()));
			effectEndPointComboBox.setSelectedItem(getLocationString(cEffect.getEndPoint()));
			effectAnimationcomboBox.setSelectedItem(cEffect.getAnimation());
			effectIsRepeatCheckBox.setSelected(cEffect.getDuration()==0?false:true);
			if(cEffect.getDuration()!=0)
				effectDurationTextField.setValue(cEffect.getDuration());
			
			effectDurationLabel.setEnabled(effectIsRepeatCheckBox.isSelected());
			effectDurationTextField.setEnabled(effectIsRepeatCheckBox.isSelected());
		}
		
		else if(sTime instanceof CmdCamera){
			CmdCamera cCamera = (CmdCamera) sTime;
			cameraTargetComboBox.setSelectedIndex(cCamera.getTarget());
			cameraStartTimeTextField.setValue(cCamera.getStartTime());
			cameraDurationTextField.setValue(cCamera.getDuration());
			cameraZoomTextField.setValue(cCamera.getZoom());
		}
		
		else if(sTime instanceof CmdShock){
			CmdShock cShock = (CmdShock) sTime;
			shockStartTimeTextField.setValue(cShock.getStartTime());
			shockDurationTextField.setValue(cShock.getDuration());
			shockLevelTextField.setValue(cShock.getLevel());
		}
		
		else if(sTime instanceof CmdHit){
			CmdHit cHit = (CmdHit) sTime;
			hitIsSelfComboBox.setSelectedIndex(cHit.isSelf()==true ? 0 : 1);
			hitStartTimeTextField.setValue(cHit.getStartTime());
			hitFontSizeTextField.setValue(cHit.getFontSize());
			hitTimesTextField.setValue(cHit.getTimes());
			hitIntervalTextField.setValue(cHit.getInterval());
		}
		
		else if(sTime instanceof CmdSound){
			CmdSound cSound = (CmdSound) sTime;
			soundStratTimeTextField.setValue(cSound.getStartTime());
			soundFileNamecomboBox.setSelectedItem(cSound.getSoundFileName());
		}
		
		else if(sTime instanceof CmdPeriod){
			CmdPeriod cPeriod = (CmdPeriod) sTime;
			periodDurationTextField.setValue(cPeriod.getDuration());
		}
	}

	private static int getLocationIndex(String str){
		String[] tmp = str.split("_");
		return Integer.valueOf(tmp[0]);
	}

	private static String getLocationString(int idx){
		for(int i=0; i<ARRAY_EFFECT_LOCATION.length; i++){
			if(getLocationIndex(ARRAY_EFFECT_LOCATION[i])==idx)
				return ARRAY_EFFECT_LOCATION[i];
		}
		return "";
	}

	class MyListener implements KeyListener, TimeBarSelectionListener, 
		TimeBarModelListener{
		
		
		public void keyPressed(KeyEvent e) {
			
			if (e.getKeyCode() == KeyEvent.VK_DOWN && isCtrlPressed == false)
				selectInterval(1);
	
			if (e.getKeyCode() == KeyEvent.VK_DOWN && isCtrlPressed == true)
				rowBarModel.moveRow(selectedRow, 1);
	
			if (e.getKeyCode() == KeyEvent.VK_UP && isCtrlPressed == false)
				selectInterval(-1);
	
			if (e.getKeyCode() == KeyEvent.VK_UP && isCtrlPressed == true)
				rowBarModel.moveRow(selectedRow, -1);
			
			if (e.getKeyCode() == KeyEvent.VK_RIGHT && isCtrlPressed == true)
				moveCmd(0.02);
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT && isCtrlPressed == true)
				moveCmd(-0.02);
			
			if(e.getKeyCode()==KeyEvent.VK_DELETE)
				removeCmd();
				
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				isCtrlPressed = true;
			
			if (e.getKeyCode() == KeyEvent.VK_ALT)
				isAltPressed = true;
			
//			if (e.getKeyCode() == KeyEvent.VK_C && isCtrlPressed == true)
//				copiedRow = selectedRow;
	
			if (e.getKeyCode() == KeyEvent.VK_V && isCtrlPressed == true)
				copyCmd();
		}
	
		
		public void keyReleased(KeyEvent e) {
			
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				isCtrlPressed = false;
			
			if (e.getKeyCode() == KeyEvent.VK_ALT)
				isAltPressed = false;
		}
	
		
		public void keyTyped(KeyEvent e) {
	
		}
	
		
		public void elementAddedToSelection(TimeBarSelectionModel arg0,	Object arg1) {
		}
	
		
		public void elementRemovedFromSelection(TimeBarSelectionModel arg0,	Object arg1) {
		}
	
		
		public void selectionChanged(TimeBarSelectionModel selectionModel) {
	
			int count = selectionModel.getSelectedIntervals().size(); 
			
			modifyCmdButton.setEnabled(count == 0 ? false : true);
			
			if (count == 0) {
				selectedRow = null;
				actionStartTimeTextField.setValue(0f);
				actionDelayTextField.setValue(0f);
				switchTabbedPanel(Skill.CMD_ACTION);
				return;
			}
	
			timeBarViwer.requestFocus();
			CmdInterval cInterval = (CmdInterval) selectionModel.getSelectedIntervals().get(0);
			selectedRow = (CmdTimeBarRow) rowBarModel.getRowForInterval(cInterval);
			newCmdButton.setEnabled(selectedRow.getCmd() instanceof CmdPeriod ? false : true);
			
			writeDataToUI(selectedRow.getCmd());
			switchTabbedPanel(selectedRow.getCmd().getCmdType());
		}
	
		
		public void elementAdded(TimeBarModel arg0, TimeBarRow arg1, Interval arg2) {
		}
	
		
		public void elementChanged(TimeBarModel arg0, TimeBarRow row, Interval interval) {
			if (isUpdatingAction == true)
				return;
			if (selectedRow == null || (selectedRow.getCmd() instanceof IStartTime) ==false)
				return;
			
			JaretDate begin = interval.getBegin();
			double milliseconds = (new BigDecimal(begin.getMillis() / 1000f)
					.setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue(); 
			
			IStartTime its = (IStartTime) selectedRow.getCmd();
			its.setStartTime(begin.getSeconds()+milliseconds);
			writeDataToUI(its);
		}
	
		
		public void elementRemoved(TimeBarModel arg0, TimeBarRow arg1, Interval arg2) {
		}
	
		
		public void headerChanged(TimeBarModel arg0, TimeBarRow arg1, Object arg2) {
		}
	
		
		public void modelDataChanged(TimeBarModel model) {
		}
	
		
		public void rowAdded(TimeBarModel arg0, TimeBarRow raw) {
			if (isOpening == false)
				timeBarViwer.getSelectionModel().setSelectedInterval(raw.getIntervals().get(0));
		}
	
		
		public void rowDataChanged(TimeBarModel arg0, TimeBarRow arg1) {
		}
	
		
		public void rowRemoved(TimeBarModel arg0, TimeBarRow arg1) {
		}
	}

	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, e.getStackTrace()[0], "錯誤",
				JOptionPane.ERROR_MESSAGE);
	}


}
