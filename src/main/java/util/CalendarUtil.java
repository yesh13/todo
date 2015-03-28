package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {
	public static Calendar string2calendar(String s) {
		if(s==null) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(s));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return cal;
	}
}
