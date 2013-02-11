package game.skill.bar;

import game.skill.CmdAction;
import game.skill.CmdCamera;
import game.skill.CmdEffect;
import game.skill.CmdHit;
import game.skill.CmdMove;
import game.skill.CmdPeriod;
import game.skill.CmdShock;
import game.skill.CmdSound;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import de.jaret.util.date.Interval;
import de.jaret.util.swing.GraphicsHelper;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.TimeBarRenderer;

public class CmdTimeBarRenderer implements TimeBarRenderer {
	
	private static Color COLOR_SHOCK = new Color(0,0,200);
	private static Color COLOR_CAMERA = new Color(150,150,255); 
	private static Color COLOR_ACTION = new Color(225,130,00);
	private static Color COLOR_EFFECT = new Color(128,0,128);
	private static Color COLOR_SOUND = Color.yellow; 
	private static Color COLOR_HIT = Color.gray;
	private static Color COLOR_MOVE = Color.green;
	
	SkillRendererComponent _eventComponent;
	
	

	public CmdTimeBarRenderer() {
		_eventComponent = new SkillRendererComponent();
	}

	public JComponent getTimeBarRendererComponent(TimeBarViewer tbv,
			Interval value, boolean isSelected, boolean overlapping) {

		_eventComponent.setInterval((CmdInterval) value);
		_eventComponent.setSelected(isSelected);
		return _eventComponent;

	}

	public class SkillRendererComponent extends JComponent {
		CmdInterval _interval;
		boolean _selected;

		public SkillRendererComponent() {
			setLayout(null);
			setOpaque(false);
		}

		public void setInterval(CmdInterval interval) {
			_interval = interval;
		}

		public String getToolTipText() {
			return "<html><b>" + _interval.getTitle() + "</b></html>";
		}

		public void setSelected(boolean selected) {
			_selected = selected;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int height = getHeight();
			int width = getWidth();

			if (_interval.getCmd() instanceof CmdAction)
				g.setColor(COLOR_ACTION);

			else if (_interval.getCmd() instanceof CmdMove)
				g.setColor(COLOR_MOVE);

			else if (_interval.getCmd() instanceof CmdEffect)
				g.setColor(COLOR_EFFECT);

			else if (_interval.getCmd() instanceof CmdCamera)
				g.setColor(COLOR_CAMERA);

			else if (_interval.getCmd() instanceof CmdShock)
				g.setColor(COLOR_SHOCK);

			else if (_interval.getCmd() instanceof CmdHit)
				g.setColor(COLOR_HIT);

			else if (_interval.getCmd() instanceof CmdSound)
				g.setColor(COLOR_SOUND);

			else if (_interval.getCmd() instanceof CmdPeriod)
				g.setColor(Color.black);
			
			g.fillRect(0, 0, width - 1, height - 1);
			
			if(_selected==true){
				g.setColor(Color.red);
				g.drawRect(0, 0, width - 1, height - 1);
				g.drawRect(1, 1, width - 3, height - 3);
				g.drawRect(2, 2, width - 5, height - 5);
			}
			else{
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, width - 1, height - 1);
			}


			GraphicsHelper.drawStringCenteredVCenter(g, _interval.getTitle(),
					0, width, height / 2);
		}

		public boolean contains(int x, int y) {
			if (y >= 0 && y <= getHeight()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Rectangle getPreferredDrawingBounds(Rectangle intervalDrawingArea,
			TimeBarViewerDelegate delegate, Interval interval,
			boolean selected, boolean overlap) {
		return intervalDrawingArea;
	}

}
