package domain;

import java.util.Calendar;

public class Schedule {
	private Calendar startTime;
	private Calendar endTime;
	private int type=0;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Calendar getStartTime() {
		return startTime;
	}
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	public Calendar getEndTime() {
		return endTime;
	}
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	public boolean withinTime(Calendar t1, Calendar t2) {
		switch(type){
		case 0:
			if(endTime==null){
				return t2.after(startTime);			}
			else{
				return endTime.before(t2)&&endTime.after(t1);
			}
		case 1:
			return t1.before(endTime)&&startTime.before(t2);
		case 2:
			if(startTime==null){
				return t1.before(endTime);
			}else{
				return endTime.before(t2)&&endTime.after(t1);
			}
			default:
				return false;
		}
	}
}
