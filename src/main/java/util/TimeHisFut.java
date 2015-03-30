package util;

import java.util.Calendar;
//no need
public class TimeHisFut {
	public static boolean getFuture(Calendar early,Calendar late){
		Calendar today=Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY,0);
		today.add(Calendar.HOUR_OF_DAY, -11);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND,0);
		return early.after(today);
	}
	public static boolean getHistory(Calendar early,Calendar late){
		Calendar today=Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY,13);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND,0);
		return late.before(today);
	}
}
