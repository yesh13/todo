package application.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.Activity;
import domain.ActivityFactory;
import domain.Appointment;
import domain.BasicActivity;
import domain.Task;

public class ActivityAdapter extends TypeAdapter<Activity> {
	public void basicRead(JsonReader reader, Activity act) throws IOException{
		boolean out=false;
		while((!out)&&reader.hasNext()){
			String name=reader.nextName();
			//last one of basic
			if("note".equals(name)){
				out=true;
			}
			if(reader.peek()==JsonToken.NULL){
				reader.nextNull();continue;
			}
			switch(name){
			case "aid":
				act.setAid(reader.nextInt());
				break;
			case "uid":
				act.setUid(reader.nextInt());
				break;
			case "name":
				act.setName(reader.nextString());
				break;
			case "parent":
				act.setParentId(reader.nextInt());
				break;
			case "note":
				act.setDescription(reader.nextString());
				break;
			default:
				reader.skipValue();
				break;
			}
		}
	}
	@Override
	public Activity read(JsonReader reader){
		Activity act=null;
		String type=null;
		try{
		reader.beginObject();
		int aid,uid;
		if(reader.hasNext()){
			String name=reader.nextName();
			if("aid".equals(name)){
				aid=reader.nextInt();
			}else return act;
		}else return act;	
		if(reader.hasNext()){
			String name=reader.nextName();
			if("uid".equals(name)){
				uid=reader.nextInt();
			}else return act;
		}else return act;	
		act=ActivityFactory.getById(aid, uid);
		switch(act.getType()){
		case "task":
			act=new TaskAdapter().read(reader);
			break;
		case "appt":
			act=new AppointmentAdapter().read(reader);
			break;
		}
		reader.endObject();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return act;
	}

	@Override
	public void write(JsonWriter writer, Activity act) throws IOException {
		// TODO Auto-generated method stub
		writer.name("aid").value(act.getAid());
		writer.name("uid").value(act.getUid());
		writer.name("type").value(act.getType());
		writer.name("name").value(act.getName());
		writer.name("parent").value(act.getParentId());
		writer.name("note").value(act.getDescription());
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
		writer.name("subPend");
		writer.beginArray();
		for(Activity bact: act.getSubPend()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();
	}

}
