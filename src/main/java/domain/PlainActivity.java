package domain;

import java.util.Calendar;

public class PlainActivity extends Activity {

	@Override
	public boolean withinTime(Calendar t1, Calendar t2) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSortTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean allowSubTask() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean allowSubAppt() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean allowSubNote() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean allowSubPend() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "plain";
	}

}
