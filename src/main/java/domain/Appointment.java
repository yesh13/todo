package domain;

import java.util.Calendar;

public class Appointment extends Activity{
	private Calendar startTime;
	private Calendar finishTime;
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "appt";
	}

	@Override
	public boolean withinTime(Calendar t1, Calendar t2) {
		// TODO Auto-generated method stub
		return t1.before(finishTime)&&startTime.before(t2);
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSortTime() {
		// TODO Auto-generated method stub
		return isFinished()?finishTime.getTimeInMillis():startTime.getTimeInMillis();
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return finishTime!=null&&Calendar.getInstance().after(finishTime);
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return true;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Calendar finishTime) {
		this.finishTime = finishTime;
	}
	
}
