package application.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import domain.Activity;



public class SubActivityAdapter extends TypeAdapter<Activity> {

	@Override
	public Activity read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(JsonWriter writer, Activity act) throws IOException {
		// TODO Auto-generated method stub
		writer.beginObject();
		writer.name("name").value(act.getName());
		writer.name("aid").value(act.getAid());
		writer.name("type").value(act.getType());
		writer.endObject();
		
	}

}
