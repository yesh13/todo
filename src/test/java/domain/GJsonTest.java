package domain;

import static org.junit.Assert.*;

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
		System.out.println("write");
		Gson gson =new GsonFactory().getActivity();
		Activity act=ActivityFactory.getById(486, 1);
		Calendar t1=Calendar.getInstance();
		t1.set(2015, 2,22);
		Calendar t2=Calendar.getInstance();
		t2.set(2015, 4, 26);
		RequestFilter rf=new RequestFilter();
		rf.setT1(t1);
		rf.setT2(t2);
		act.fetchChild(rf);
		Activity act2=ActivityFactory.getById(488, 1);
		System.out.println(gson.toJson(act));
		System.out.println(gson.toJson(act2));
	}
	@Test
	public void read(){
		System.out.println("read");
		Gson gson =new GsonFactory().getActivity();
		Activity act=ActivityFactory.getById(486, 1);
		Calendar t1=Calendar.getInstance();
		t1.set(2015, 2,22);
		Calendar t2=Calendar.getInstance();
		t2.set(2015, 4, 26);
		Task newAct=(Task)gson.fromJson(gson.toJson(act), Activity.class);
		assertEquals(newAct.getName(),act.getName());
		assertEquals(newAct.getParentId(),0);
		assertEquals(newAct.getState(),Task.State.Active);
		String trial="{\"type\":\"task\",\"aid\":486,\"uid\":1,\"name\":\"something\",\"parent\":0,\"note\":\"aaaaa\",\"subTask\":[],\"subAppt\":[],\"subNote\":[],\"subPend\":[],\"startTime\":null,\"finishTime\":null}";
		Activity act2=gson.fromJson(trial, Activity.class);
		//act2.updateActivity();
		//System.out.println(gson.toJson(act2));
	}

}
