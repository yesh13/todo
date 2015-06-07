package application.gson;

import java.io.IOException;

import util.CalendarUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.Activity;
import domain.Appointment;
import domain.PlainActivity;

public class PlainActivityAdapter extends TypeAdapter<PlainActivity> {

	@Override
	public void write(JsonWriter writer, PlainActivity act) throws IOException {
		writer.beginObject();
		(new ActivityAdapter()).write(writer, act);
		writer.endObject();
	}

	@Override
	public PlainActivity read(JsonReader reader) throws IOException {
		Activity activity=new ActivityAdapter().basicRead(reader);
		if(activity==null||!"plain".equals(activity.getType()))return null;
		PlainActivity act=(PlainActivity) activity;
		while(reader.hasNext()){
			String name=reader.nextName();
			if(reader.peek()==JsonToken.NULL){
				reader.nextNull();continue;
			}
			switch(name){
			default:
				reader.skipValue();
				break;
			}
		}
		return act;
	}

}
