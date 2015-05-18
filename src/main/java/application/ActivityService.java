package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.hibernate.HibernateFactory;
import domain.Activity;
import domain.ActivityFactory;
import domain.ListComparator;
import domain.Schedule;

public class ActivityService {
	int user = 1;
	Account account;
	public ActivityService(int user){
		this.user=user;
		account=Account.read(user);
	}

	public ActivityDTO getActivity(String aidString, int uid, boolean withNote) {
		int aid = Integer.parseInt(aidString);
		Activity act = ActivityFactory.getById(aid, user);
		return new ActivityDTO(act, withNote);
	}

	private Calendar string2calendar(String s) {
		return util.CalendarUtil.string2calendar(s);
	}

	public int addActivity(ActivityDTO dto, int uid) {
/*		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Activity act = new Activity();
			act.setName(dto.getName());
			act.setNote(new Note(dto.getNote()));
			act.setLocation(new Location(dto.getLocation()));
			act.setParent(Integer.parseInt(dto.getParent()));
			act.setUid(uid);
			act.setType(Integer.valueOf(dto.getType()));
					act.getSchedule().setStartTime(
							string2calendar(dto.getStartTime()));
			
					act.getSchedule().setEndTime(
							string2calendar(dto.getEndTime()));
					act.getSchedule().setFinishTime(
							string2calendar(dto.getFinishTime()));
			
			session.save(act);
			tx.commit();
			account.setUpdate();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
*/
		return 0;
	}

	// public int deleteActivity(ActivityDTO dto){
	// Activity act=new Activity();
	// act.setName(dto.getName());
	// act.setNote(new Note(dto.getNote()));
	// act.setLocation(new Location(dto.getLocation()));
	// act.setParent(new ActivityId(dto.getParent()));
	// (new ActivityDao()).insert(act, user);
	// return 0;
	// }

	// return number of rows affected
	public int editActivity(ActivityDTO dto, int uid) {
/*		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		List<Activity> alist = null;
		try {
			tx = session.beginTransaction();
			alist = session
					.createQuery(
							"from Activity act where act.aid = :aid and act.uid = :uid")
					.setInteger("aid", Integer.valueOf(dto.getAid()))
					.setInteger("uid", user).list();
			if (alist.size() == 0) {
				return 0;
			}
			Activity act = alist.get(0);
			if (dto.getName() != null)
				act.setName(dto.getName());
			if (dto.getNote() != null)
				act.setNote(new Note(dto.getNote()));
			if (dto.getLocation() != null)
				act.setLocation(new Location(dto.getLocation()));
			if (dto.getParent() != null)
				act.setParent(Integer.parseInt(dto.getParent()));
			if (dto.getUid() != null)
				act.setUid(user);
			if(dto.getType()!=null) act.setType(Integer.valueOf(dto.getType()));
			
				
					
						act.getSchedule().setStartTime(
								string2calendar(dto.getStartTime()));
					
				
		
						act.getSchedule().setEndTime(
								string2calendar(dto.getEndTime()));
						act.getSchedule().setFinishTime(
								string2calendar(dto.getFinishTime()));
				
			
			tx.commit();
			account.setUpdate();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}*/
		return 1;
	}



	public List<Activity> getPath(String aidString, int uid) {
		ArrayList<Activity> dlist = new ArrayList<Activity>();
		int curId = Integer.parseInt(aidString);
		while (curId!=0) {
			Activity act=ActivityFactory.getById(curId,uid);
			if (act!=null) {
				dlist.add(act);
				curId = act.getParentId();
			} else {
				return new ArrayList<Activity>();
			}
		}
		Collections.reverse(dlist);
		return dlist;
	}


	public List<ActivityDTO> getAll(int uid) {
		List<ActivityDTO> dlist = new ArrayList<ActivityDTO>();
		HibernateFactory instance = util.hibernate.HibernateFactory
				.getInstance();
		SessionFactory sessionFactory = instance.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Activity> alist = null;
		try {
			tx = session.beginTransaction();
			alist = session
					.createQuery("from Activity act where act.uid = :uid")
					.setInteger("uid", user).list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		for (Activity act : alist) {
			ActivityDTO dto = new ActivityDTO(act, false);
			dlist.add(dto);
		}
		return dlist;
	}
	public void updateAccount(){
		account.setUpdate();
	}
}
