package domain;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Activity {
	private int aid;
	private Location location=new Location("");
	private String name;

	
	public void setType(int type) {
		this.schedule.setType(type);
	}
	private Note note=new Note("");

	private int parent;

	private Schedule schedule=new Schedule();

	private int uid;
	public Activity() {
		super();
		schedule=new Schedule();
	}
	public Activity(int aid) {
		super();
		this.aid = aid;
	}
	public int getAid() {
		return aid;
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Note getNote() {
		return note;
	}


	public int getParent() {
		return parent;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public int getUid() {
		return uid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	public static Activity getById(int aid,int uid){
		Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      List<Activity> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("from Activity act where act.aid = :aid and act.uid = :uid")
	        		 .setInteger("aid", aid).setInteger("uid", uid).list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      if(alist.size()==0){
	    	  return null;
	      } else{
	    	  return alist.get(0);
	      }
	}
	public boolean isDeepChildOf(int root) {
		int curId = aid;
		while (curId!=0) {
			Activity act = Activity.getById(curId, uid);
			if(act==null){
				return false;
			}
			curId=act.getParent();
			if (curId==root) {
				return true;
			}
		}
		return false;
	}
	public int getType() {
		// TODO Auto-generated method stub
		return this.schedule.getType();
	}
	public boolean withinTime(Calendar t1,Calendar t2){
		return schedule.withinTime(t1,t2);
	}
	private int priority=-1;
	public int getPriority() {
		// TODO Auto-generated method stub
		if(priority==-1){
			switch(getSchedule().getType()){
			case 0:
				if(getSchedule().getEndTime()!=null){
					priority=50;
				}else if(getSchedule().getStartTime()!=null){
					priority=30;
				}else{
					priority=100;
				}
				break;
			case 1:
				priority=11;
				break;
			case 2:
				if(getSchedule().getStartTime()!=null){
					priority=50;
				}else{
					Calendar c=Calendar.getInstance();
					c.add(Calendar.HOUR_OF_DAY, 24);
					if(getSchedule().getEndTime().after(c)){
						priority=40;
					}else {
						priority=0;
					}	
				}
				break;
			}
		}
		return priority;
	}
	private long sortTime=-100;
	public long getSortTime() {
		if(sortTime==-100){
			switch(getSchedule().getType()){
			case 0:
				if(getSchedule().getEndTime()!=null){
					sortTime=getSchedule().getEndTime().getTimeInMillis();
				}else if(getSchedule().getStartTime()!=null){
					sortTime=getSchedule().getStartTime().getTimeInMillis();
				}else{
					sortTime=aid;
				}
				break;
			case 1:
				sortTime=getSchedule().getStartTime().getTimeInMillis();
				break;
			case 2:
				if(getSchedule().getStartTime()!=null){
					sortTime=getSchedule().getStartTime().getTimeInMillis();
				}else{
					sortTime=getSchedule().getEndTime().getTimeInMillis();
				}
				break;
			}
		}
		return sortTime;
	}
}
