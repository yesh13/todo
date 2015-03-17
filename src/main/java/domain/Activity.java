package domain;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.Account;

public class Activity {
	private int aid;
	private Location location=new Location("");
	private String name;

	private Note note=new Note("");

	private int parent;

	private Schedule schedule=new Schedule();

	private int uid;
	public Activity() {
		super();
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
}
