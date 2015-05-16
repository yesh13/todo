package domain;

import java.util.Calendar;

public interface Schedule {
	public abstract boolean withinTime(Calendar t1,Calendar t2);
	public abstract boolean isFinished();
	public abstract boolean isActive();
	abstract public int getPriority();
	abstract public long getSortTime() ;
}
