package domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

public class ActivityTest {
@Test
public void deepChild(){
	Activity act=Activity.getById(24,1);
	assertTrue(act.isDeepChildOf(0));
	assertTrue(act.isDeepChildOf(22));
	assertFalse(act.isDeepChildOf(1));
	Activity act1=Activity.getById(24,1);
	//TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	System.out.println(act1.getSchedule().getStartTime());
	System.out.println((new Date()).toString());
	System.out.println(new Timestamp((new Date()).getTime()));
}
}
