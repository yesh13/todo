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
import application.Account;
import application.AccountFactory;
import application.ActivityDTO;

public abstract class Activity extends BasicActivity implements Schedule{
	private String description;
	private ArrayList<Activity> subAppt=null;
	private ArrayList<Activity> subTask=null;
	private ArrayList<Activity> subNote=null;
	private ArrayList<Activity> subPend=null;
	public abstract boolean allowSubTask();
	public abstract boolean allowSubAppt();
	public abstract boolean allowSubNote();
	public abstract boolean allowSubPend();
	public int updateActivity(){
		int ret=0;
		if(getAid()==0)return ret;
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(this);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			ret=1;
		} finally {
			session.close();
		}
		if(ret==0){
			Account ac=new AccountFactory().getUser();
			if(ac!=null)ac.update();
		}
		return ret;
	}

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
	public void fetchChild(RequestFilter rf){
		Calendar t1=rf.getT1();
		Calendar t2=rf.getT2();
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
		if(rf.allowAppt())subAppt=new ArrayList<Activity>();
		if(rf.allowNote())subNote=new ArrayList<Activity>();
		if(rf.allowPend())subPend=new ArrayList<Activity>();
		if(rf.allowTask())subTask=new ArrayList<Activity>();
		//unsortedList
		for (Activity act : alist) {
			if(!act.withinTime(t1, t2)) continue;
			if(!act.isDeepChildOf(getAid()))continue;
			if(rf.allowNote()&&act.isFinished()){
				subNote.add(act);
			}else if(act.isActive()){
			if(rf.allowTask()&&act.getType().equals("task")){
					subTask.add(act);
			}else if(rf.allowAppt()&&act.getType().equals("appt")){
				subAppt.add(act);
			}
			}else{
				if(rf.allowPend())
				subPend.add(act);
			}
		}
		ListComparator comp=new ListComparator();
		if(subTask!=null)Collections.sort(subTask, comp);
		if(subAppt!=null)Collections.sort(subAppt, comp);
		if(subNote!=null)Collections.sort(subNote, comp);
		if(subPend!=null)Collections.sort(subPend, comp);
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
