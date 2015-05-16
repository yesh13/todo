package domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.hibernate.HibernateFactory;
import application.ActivityDTO;

public abstract class Activity extends BasicActivity implements Schedule{
	private String description;
	private ArrayList<Activity> subAppt=new ArrayList<Activity>();
	private ArrayList<Activity> subTask=new ArrayList<Activity>();
	private ArrayList<Activity> subNote=new ArrayList<Activity>();

	public Activity() {
		super();
	}
	public abstract String getType();

	public ArrayList< Activity> getSubAppt() {
		return subAppt;
	}
	public void setSubAppt(ArrayList< Activity> subAppt) {
		this.subAppt = subAppt;
	}
	public ArrayList<Activity> getSubTask() {
		return subTask;
	}
	public void setSubTask(ArrayList<Activity> subTask) {
		this.subTask = subTask;
	}
	public ArrayList<Activity> getSubNote() {
		return subNote;
	}
	public void setSubNote(ArrayList< Activity> subNote) {
		this.subNote = subNote;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void fetchChild(Calendar t1,Calendar t2){
		HibernateFactory instance = util.hibernate.HibernateFactory
				.getInstance();
		SessionFactory sessionFactory = instance.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Activity> alist = null;
		try {
			tx = session.beginTransaction();
			alist = session
					.createQuery(
							"from Activity act where act.parentId = :aid and act.uid = :uid")
					.setInteger("aid", getAid())
					.setInteger("uid", getUid()).list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		//unsortedList
		for (Activity act : alist) {
			if(!act.withinTime(t1, t2)) continue;
			if(act.isFinished()){
				subNote.add(act);
			}else if(act.isActive()){
			if(act.getType().equals("task")){
					subTask.add(act);
			}else if(act.getType().equals("appt")){
				subAppt.add(act);
			}
			}
		}
		ListComparator comp=new ListComparator();
		Collections.sort(subTask, comp);
		Collections.sort(subAppt, comp);
		Collections.sort(subNote, comp);
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
		int curId = getAid();
		while (curId!=0) {
			Activity act = Activity.getById(curId, getUid());
			if(act==null){
				return false;
			}
			curId=act.getParentId();
			if (curId==root) {
				return true;
			}
		}
		return false;
	}
}
