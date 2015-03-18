package domain;

import java.util.Calendar;

public class DateActivityFilter implements ActivityFilter {
	private Calendar early;
	public DateActivityFilter(Calendar early, Calendar late) {
		super();
		this.early = early;
		this.late = late;
	}
	private Calendar late;
	//return true when pass
	@Override
	public boolean test(Activity act) {
		if(act.getSchedule().getStartTime().before(late)){
			if(act.getSchedule().getEndTime()==null||act.getSchedule().getEndTime().after(early)){
				return true;
			}
		}
		return false;
	}
}
