package domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ActivityTest {
@Test
public void deepChild(){
	Activity act=Activity.getById(487,1);
	assertFalse(act==null);
	if(act==null)return;
	assertTrue(act.isDeepChildOf(0));
	assertFalse(act.isDeepChildOf(2));
	assertTrue(act.getName().equals("123123123"));
	assertTrue(act.getType().equals("task"));
	act=Activity.getById(488,1);
	assertTrue(act.getType().equals("appt"));
}
@Test
public void fetchChild(){
	Activity act=Activity.getById(486,1);
	assertFalse(act==null);
	if(act==null)return;
	assertTrue(act.getName().equals("1111112222"));
	Calendar t1=Calendar.getInstance();
	t1.set(2015, 2,22);
	Calendar t2=Calendar.getInstance();
	t2.set(2015, 4, 26);
	if(act==null||t1==null||t2==null)
		System.err.println("null time");
	else{
	act.fetchChild(t1,t2);
	assertFalse(act.isActive());
	assertTrue(act.getSubTask().size()==1);
	assertTrue(act.getSubNote().size()==2);
	assertTrue(act.getSubAppt().size()==1);
	}
}

}
