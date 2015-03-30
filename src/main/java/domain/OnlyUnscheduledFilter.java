package domain;

public class OnlyUnscheduledFilter implements ActivityFilter {
	private boolean invert=false;
	public OnlyUnscheduledFilter(boolean invert){
		this.invert=invert;
	}
	@Override
	public boolean test(Activity act) {
		boolean x= act.getSchedule().getType()==0&&act.getSchedule().getEndTime()==null&&act.getSchedule().getStartTime()==null;
		return invert?!x:x;
	}

}
