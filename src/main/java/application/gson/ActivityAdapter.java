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
	public Activity basicRead(JsonReader reader) throws IOException{
		boolean out=false;
		Activity act=null;
		int aid=0,uid;
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
				act=ActivityFactory.getById(aid, uid);
			}else return act;
		}else return act;
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
			case "name":
				act.setName(reader.nextString());
				break;
			case "parent":
				reader.skipValue();
				break;
			case "note":
				act.setDescription(reader.nextString());
				break;
			default:
				reader.skipValue();
				break;
			}
		}
		return act;
	}
	@Override
	public Activity read(JsonReader reader){
		Activity act=null;
		String type=null;
		try{
		reader.beginObject();
		if(reader.hasNext()){
			String name=reader.nextName();
			if("type".equals(name)){
				type=reader.nextString();
			}else return act;
		}else return act;	
		/*if(reader.hasNext()){
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
		}else return act;*/	
		switch(type){
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
		writer.name("type").value(act.getType());
		writer.name("aid").value(act.getAid());
		writer.name("uid").value(act.getUid());
		writer.name("name").value(act.getName());
		writer.name("parent").value(act.getParentId());
		writer.name("note").value(act.getDescription());
		SubActivityAdapter subAdapter=new SubActivityAdapter();
		writer.name("subTask");
		if(act.getSubTask()!=null){
		writer.beginArray();
		for(Activity bact: act.getSubTask()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();}
		else writer.nullValue();
		
		writer.name("subAppt");
		if(act.getSubAppt()!=null){
		writer.beginArray();
		for(Activity bact: act.getSubAppt()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();}
		else writer.nullValue();
		
		writer.name("subNote");
		if(act.getSubNote()!=null){
		writer.beginArray();
		for(Activity bact: act.getSubNote()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();}
		else writer.nullValue();
		
		writer.name("subPend");
		if(act.getSubPend()!=null){
		writer.beginArray();
		for(Activity bact: act.getSubPend()){
			subAdapter.write(writer, bact);
		}
		writer.endArray();}
		else writer.nullValue();
	}

}
