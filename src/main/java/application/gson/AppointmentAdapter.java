package application.gson;

import java.io.IOException;

import util.CalendarUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import domain.Appointment;

public class AppointmentAdapter extends TypeAdapter<Appointment> {

	@Override
	public Appointment read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(JsonWriter writer, Appointment act) throws IOException {
		writer.beginObject();
		(new ActivityAdapter()).write(writer, act);
		writer.name("startTime");
		CalendarUtil.calendar2Gson(writer,act.getStartTime());
		writer.name("finishTime");
		CalendarUtil.calendar2Gson(writer,act.getFinishTime());
		writer.endObject();
		
	}

}
