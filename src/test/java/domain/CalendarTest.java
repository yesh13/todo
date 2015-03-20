package domain;

import static org.junit.Assert.assertFalse;

import java.util.Calendar;

import org.junit.Test;

public class CalendarTest {
@Test
public void before(){
	assertFalse(Calendar.getInstance().before(null));
}
}
