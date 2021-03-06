package domain;

import java.util.Calendar;
import java.util.Comparator;

public class LeavesComparator implements Comparator<Activity> {

	@Override
	public int compare(Activity o1, Activity o2) {
		// TODO Auto-generated method stub
		int p1=o1.getPriority();
		int p2=o2.getPriority();
		if(p1==p2){
			int sign=(p1%2==0)?1:-1;
			long t1=o1.getSortTime();
			long t2=o2.getSortTime();
			if(t1<t2){
				return -sign;
			}else if(t1>t2){
				return sign;
			}else{
				return 0;
			}
		}else if(p1<p2){
			return -1;
		}else{
			return 1;
		}
	}

}
