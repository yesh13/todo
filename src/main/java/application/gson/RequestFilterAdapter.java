package application.gson;

import java.io.IOException;
import java.util.Calendar;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import domain.RequestFilter;

public class RequestFilterAdapter extends TypeAdapter<RequestFilter> {

	@Override
	public void write(JsonWriter out, RequestFilter value) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public RequestFilter read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		RequestFilter rf=new RequestFilter();
		in.beginObject();
		while(in.hasNext()){
			String name=in.nextName();
			if(in.peek()==JsonToken.NULL){
				in.nextNull();
				continue;
			}
			switch(name){
			case "t1":
				Calendar c1=Calendar.getInstance();
				c1.setTimeInMillis(in.nextLong());
				rf.setT1(c1);
				break;
			case "t2":
				Calendar c2=Calendar.getInstance();
				c2.setTimeInMillis(in.nextLong());
				rf.setT2(c2);
				break;
			case "sub":
				rf.setSub(in.nextInt());
				break;
				default:
					in.skipValue();
					break;
			}
		}
		in.endObject();
		return rf;
	}

}
