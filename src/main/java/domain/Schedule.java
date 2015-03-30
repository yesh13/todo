package domain;

import java.util.Calendar;
import util.TimeHisFut;

public class Schedule {
	private Calendar startTime;
	private Calendar endTime;
	private Calendar finishTime;
	public Calendar getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Calendar finishTime) {
		this.finishTime = finishTime;
	}
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
		Calendar today=Calendar.getInstance();
		boolean future=t1.after(today);
		boolean history=t2.before(today);
		switch(type){
		case 0:
			if(endTime==null){
				//unfinished
				return !history&&(!future||t1.before(startTime))&&t2.after(startTime);	
				}
			else{
				//finished
				return endTime.before(t2)&&endTime.after(t1);
			}
		case 1:
			return t1.before(endTime)&&startTime.before(t2);
		case 2:
			//unfinished
			if(finishTime==null){
				boolean b=true;
				if(startTime!=null){
					startTime.before(t2);
				}
				return b&&!history&&t1.before(endTime);
			}else{
				//finish
				return finishTime.before(t2)&&finishTime.after(t1);
			}
			default:
				return false;
		}
	}
}
