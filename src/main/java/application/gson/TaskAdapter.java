package application.gson;

import java.io.IOException;
import java.util.Calendar;

import util.CalendarUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
	public Task read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
