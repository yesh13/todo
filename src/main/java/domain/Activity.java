package domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.PostLoad;

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
	private ArrayList<Activity> subPend=new ArrayList<Activity>();
	


	public ArrayList<Activity> getSubPend() {
		return subPend;
	}
	public void setSubPend(ArrayList<Activity> subPend) {
		this.subPend = subPend;
	}
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
							"from Activity act where act.uid = :uid")
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
			if(!act.isDeepChildOf(getAid()))continue;
			if(act.isFinished()){
				subNote.add(act);
			}else if(act.isActive()){
			if(act.getType().equals("task")){
					subTask.add(act);
			}else if(act.getType().equals("appt")){
				subAppt.add(act);
			}
			}else{
				subPend.add(act);
			}
		}
		ListComparator comp=new ListComparator();
		Collections.sort(subTask, comp);
		Collections.sort(subAppt, comp);
		Collections.sort(subNote, comp);
		Collections.sort(subPend, comp);
	}
	public boolean isDeepChildOf(int root) {
		int curId = getAid();
		if(curId==root)return false;
		while (curId!=0) {
			Activity act = ActivityFactory.getById(curId, getUid());
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
