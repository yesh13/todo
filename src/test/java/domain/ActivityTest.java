package domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ActivityTest {
@Test
public void deepChild(){
	Activity act=ActivityFactory.getById(487,1);
	assertFalse(act==null);
	if(act==null)return;
	assertTrue(act.isDeepChildOf(0));
	assertFalse(act.isDeepChildOf(2));
	assertTrue(act.getName().equals("123123123"));
	assertTrue(act.getType().equals("task"));
	act=ActivityFactory.getById(488,1);
	assertTrue(act.getType().equals("appt"));
}
@Test
public void fetchChild(){
	Activity act=ActivityFactory.getById(486,1);
	assertFalse(act==null);
	if(act==null)return;
	assertTrue(act.getName().equals("1111112222"));
	Calendar t1=Calendar.getInstance();
	t1.set(2015, 2,22);
	Calendar t2=Calendar.getInstance();
	t2.set(2015, 4, 26);
	RequestFilter rf=new RequestFilter();
	rf.setT1(t1);
	rf.setT2(t2);
	if(act==null||t1==null||t2==null)
		System.err.println("null time");
	else{
		Activity act2=ActivityFactory.getById(489, 1);
		assertFalse(act2.isFinished());
	act.fetchChild(rf);
	assertTrue(act.isActive());
	assertTrue(act.getSubTask().size()==1);
	assertTrue(act.getSubNote().size()==2);
	assertTrue(act.getSubAppt().size()==1);
	}
}
@Test
public void newAct(){
	Appointment apt=ActivityFactory.newAppointment(1, 0);
	assertFalse(apt.getAid()==0);
}
@Test
public void update(){
	Activity act=ActivityFactory.getById(486, 1);
	act.setName("update11");
	act.updateActivity();
	act=ActivityFactory.getById(486, 1);
	assertEquals(act.getName(),"update11");
}

}
