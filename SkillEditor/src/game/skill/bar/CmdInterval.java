package game.skill.bar;

import game.skill.ICommand;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

public class CmdInterval extends IntervalImpl {
	
    private String title="";
    private ICommand cmd;

    public CmdInterval(ICommand cmd) {
        super();
        this.cmd = cmd;
        recalculateTime();
    }


	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
    
    public ICommand getCmd(){
    	return cmd;
    }
    
    public void recalculateTime(){
        JaretDate start = new JaretDate();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds((int) cmd.getStartTime());
        start.setMilliseconds(((int) (cmd.getStartTime()*1000))%1000);
        setBegin(start);
        
        JaretDate end = new JaretDate();
        end.setHours(0);
        end.setMinutes(0);
        end.setSeconds((int) cmd.getEndTime());
        end.setMilliseconds(((int) (cmd.getEndTime()*1000))%1000);
        setEnd(end);
    }

}
