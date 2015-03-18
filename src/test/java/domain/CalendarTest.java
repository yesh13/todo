package domain;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Test;

public class CalendarTest {
@Test
public void before(){
	assertFalse(Calendar.getInstance().before(null));
}
}
