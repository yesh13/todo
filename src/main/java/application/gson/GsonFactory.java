package application.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import domain.Activity;
import domain.Appointment;
import domain.Task;

public class GsonFactory {
	public Gson getActivity(){
		GsonBuilder builder=new GsonBuilder();
		builder.registerTypeAdapter(Activity.class, new ActivityAdapter());
		builder.registerTypeAdapter(Task.class, new TaskAdapter());
		builder.registerTypeAdapter(Appointment.class, new AppointmentAdapter());
		builder.serializeNulls().setPrettyPrinting();
		return builder.create();
	}
}
