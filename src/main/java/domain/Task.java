package domain;

import java.util.Calendar;

public class Task extends Activity {
	public static enum State {
		Active,Pending
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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

	public Calendar getDeadline() {
		return deadline;
	}

	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;
	}

	private Calendar startTime;
	private Calendar finishTime;
	private Calendar deadline;
	private State state;
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "task";
	}

	@Override
	public boolean withinTime(Calendar t1, Calendar t2) {
		Calendar today=Calendar.getInstance();
		boolean future=t1.after(today);
		boolean history=t2.before(today);
		//unfinished
		if(finishTime==null){
			if(startTime!=null){
				return !history&&startTime.before(t2);
			}else return false;
			
		}else{
			//finish
			return !future&&finishTime.before(t2)&&finishTime.after(t1);
		}
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSortTime() {
		// TODO Auto-generated method stub
		if(isFinished()){
			return finishTime.getTimeInMillis();
		}else if(isPending()){
			return startTime.getTimeInMillis();
		}else{
			return startTime.getTimeInMillis();
		}
	}
	public boolean isPending(){
		return state.equals(State.Pending);
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return finishTime!=null&&Calendar.getInstance().after(finishTime);
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return !isPending()&&!isFinished();
	}

}
