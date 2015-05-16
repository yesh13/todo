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
		if(act==null) return false;
		return true;//act.withinTime(early, late);
	}
}
