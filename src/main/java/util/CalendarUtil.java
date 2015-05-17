package util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.stream.JsonWriter;

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
	public static String calendar2string(Calendar c) {
		if(c==null) return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return df.format(c.getTime());
	}
	public static void calendar2Gson(JsonWriter writer,Calendar t) throws IOException{
		if(t==null)writer.nullValue();
		else writer.value(t.getTimeInMillis());
	}
	public static Calendar long2Calendar(long time){
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(time);
		return c;
	}
}
