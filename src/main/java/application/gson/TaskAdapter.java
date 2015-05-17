package application.gson;

import java.io.IOException;
import java.util.Calendar;

import util.CalendarUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.Activity;
import domain.Task;

public class TaskAdapter extends TypeAdapter<Task> {

	@Override
	public void write(JsonWriter writer, Task act) throws IOException {
		writer.beginObject();
		(new ActivityAdapter()).write(writer, act);
		writer.name("taskState").value(act.getState().toString());
		writer.name("startTime");
		CalendarUtil.calendar2Gson(writer,act.getStartTime());
		writer.name("endTime");
		CalendarUtil.calendar2Gson(writer,act.getDeadline());
		writer.name("finishTime");
		CalendarUtil.calendar2Gson(writer,act.getFinishTime());
		writer.endObject();
	}

	@Override
	public Task read(JsonReader reader) throws IOException {
		// TODO Auto-generated method stub
		Task act=new Task();
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
			case "endTime":
				act.setDeadline(CalendarUtil.long2Calendar(reader.nextLong()));
				break;
			case "taskState":
				act.setState(Task.State.valueOf(reader.nextString()));
				break;
			default:
				reader.skipValue();
				break;
			}
		}
		return act;
	}
}
