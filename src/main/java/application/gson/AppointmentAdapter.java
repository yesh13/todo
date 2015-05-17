package application.gson;

import java.io.IOException;

import util.CalendarUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.Appointment;
import domain.Task;

public class AppointmentAdapter extends TypeAdapter<Appointment> {

	@Override
	public Appointment read(JsonReader reader) throws IOException {
		Appointment act=new Appointment();
		new ActivityAdapter().basicRead(reader, act);
		while(reader.hasNext()){
			String name=reader.nextName();
			if(reader.peek()==JsonToken.NULL){
				reader.nextNull();continue;
			}
			switch(name){
			case "startTime":
				act.setStartTime(CalendarUtil.long2Calendar(reader.nextLong()));
				break;
			case "finishTime":
				act.setFinishTime(CalendarUtil.long2Calendar(reader.nextLong()));
				break;
			}
		}
		return act;
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
