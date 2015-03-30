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
import domain.ActivityFilter;
import domain.LeavesComparator;
import domain.ListComparator;
import domain.Location;
import domain.Note;
import domain.Schedule;

public class ActivityService {
	int user = 1;

	public ActivityDTO getActivity(String aidString, int uid, boolean withNote) {
		int aid = Integer.parseInt(aidString);
		Activity act = Activity.getById(aid, uid);
		return new ActivityDTO(act, withNote);
	}

	private Calendar string2calendar(String s) {
		return util.CalendarUtil.string2calendar(s);
	}

	public int addActivity(ActivityDTO dto, int uid) {
		Session session = util.hibernate.HibernateFactory.getInstance()
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
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

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
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		List<Activity> alist = null;
		try {
			tx = session.beginTransaction();
			alist = session
					.createQuery(
							"from Activity act where act.aid = :aid and act.uid = :uid")
					.setInteger("aid", Integer.valueOf(dto.getAid()))
					.setInteger("uid", uid).list();
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
				act.setUid(uid);
			if(dto.getType()!=null) act.setType(Integer.valueOf(dto.getType()));
			
				
					
						act.getSchedule().setStartTime(
								string2calendar(dto.getStartTime()));
					
				
		
						act.getSchedule().setEndTime(
								string2calendar(dto.getEndTime()));
						act.getSchedule().setFinishTime(
								string2calendar(dto.getFinishTime()));
				
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 1;
	}

	public List<ActivityDTO> getChild(String aidString, int uid,
			List<ActivityFilter> filters) {
		ArrayList<ActivityDTO> dlist = new ArrayList<ActivityDTO>();
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
							"from Activity act where act.parent = :aid and act.uid = :uid")
					.setInteger("aid", Integer.parseInt(aidString))
					.setInteger("uid", uid).list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		//unsortedList
		ArrayList<Activity> uslist = new ArrayList<Activity>();
		for (Activity act : alist) {
			boolean pass = true;
			if (filters != null) {
				for (ActivityFilter filter : filters) {
					if (!filter.test(act)) {
						pass = false;
						break;
					}
				}
			}
			if (pass) {
					uslist.add(act);
			}
		}
		ListComparator comp=new ListComparator();
		Collections.sort(uslist, comp);
		//uslist.sort(comp);
		for (Activity act:uslist){
			dlist.add(new ActivityDTO(act,false));
		}
		return dlist;
	}

	public List<ActivityDTO> getPath(String aidString, int uid) {
		ArrayList<ActivityDTO> dlist = new ArrayList<ActivityDTO>();
		String curId = aidString;
		while (!curId.equals("0")) {
			ActivityDTO dto = getActivity(curId, uid, false);
			if (dto.getParent() != null) {
				dlist.add(dto);
				curId = dto.getParent();
			} else {
				return new ArrayList<ActivityDTO>();
			}
		}
		Collections.reverse(dlist);
		return dlist;
	}

	public List<ActivityDTO> getLeaves(String aidString, int uid,
			List<ActivityFilter> filters) {
		//unfilteredList
		ArrayList<ActivityDTO> dlist = new ArrayList<ActivityDTO>();
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
							"from Activity act where act.uid = :uid and (select count(*) from Activity where parent=act.aid)=0")
					.setInteger("uid", uid).list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		int parent = Integer.parseInt(aidString);
		//unsortedList
		ArrayList<Activity> uslist = new ArrayList<Activity>();
		for (Activity act : alist) {
			boolean pass = true;
			if (filters != null) {
				for (ActivityFilter filter : filters) {
					if (!filter.test(act)) {
						pass = false;
						break;
					}
				}
			}
			if (pass) {
				if (act.isDeepChildOf(parent)) {
					uslist.add(act);
				}
			}
		}
		LeavesComparator comp=new LeavesComparator();
		Collections.sort(uslist, comp);
		//uslist.sort(comp);
		for (Activity act:uslist){
			dlist.add(new ActivityDTO(act,false));
		}
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
					.setInteger("uid", uid).list();
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
}
