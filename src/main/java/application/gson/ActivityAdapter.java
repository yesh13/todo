package application.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import domain.Activity;
import domain.BasicActivity;

public class ActivityAdapter extends TypeAdapter<Activity> {

	@Override
	public Activity read(JsonReader arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(JsonWriter writer, Activity act) throws IOException {
		// TODO Auto-generated method stub
		writer.name("name").value(act.getName());
		writer.name("aid").value(act.getAid());
		writer.name("type").value(act.getType());
		writer.name("note").value(act.getDescription());
		writer.name("uid").value(act.getUid());
		writer.name("parent").value(act.getParentId());
		SubActivityAdapter subAdapter=new SubActivityAdapter();
		writer.name("subTask");
		writer.beginArray();
		for(Activity bact: act.getSubTask()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();
		writer.name("subAppt");
		writer.beginArray();
		for(Activity bact: act.getSubAppt()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();
		writer.name("subNote");
		writer.beginArray();
		for(Activity bact: act.getSubNote()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();
	}

}
