package domain;

import java.util.Calendar;

import org.junit.Test;

import application.gson.ActivityAdapter;
import application.gson.AppointmentAdapter;
import application.gson.GsonFactory;
import application.gson.TaskAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GJsonTest {
	@Test
	public void write(){
		Gson gson =new GsonFactory().getActivity();
		Activity act=Activity.getById(486, 1);
		Calendar t1=Calendar.getInstance();
		t1.set(2015, 2,22);
		Calendar t2=Calendar.getInstance();
		t2.set(2015, 4, 26);
		act.fetchChild(t1, t2);
		Activity act2=Activity.getById(488, 1);
		System.out.println(gson.toJson(act));
		System.out.println(gson.toJson(act2));
	}

}
