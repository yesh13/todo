package application;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import domain.Activity;
import domain.ActivityFilter;
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

	private Calendar string2calendar(String s) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(s));
		return cal;
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
			if (dto.getStartTime() != null) {
				try {
					act.getSchedule().setStartTime(
							string2calendar(dto.getStartTime()));
				} catch (java.text.ParseException e) {

				}
			} else {
				act.getSchedule().setStartTime(Calendar.getInstance());
			}
			if (dto.getEndTime() != null) {
				try {
					act.getSchedule().setEndTime(
							string2calendar(dto.getEndTime()));

				} catch (java.text.ParseException e) {

				}
			}
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
			if (dto.getStartTime() != null) {
				try {
					if (act.getSchedule() != null) {
						act.getSchedule().setStartTime(
								string2calendar(dto.getStartTime()));
					} else {
						Schedule schedule = new Schedule();
						schedule.setStartTime(string2calendar(dto
								.getStartTime()));
						act.setSchedule(schedule);
					}
				} catch (java.text.ParseException e) {

				}
			}
			if (dto.getEndTime() != null) {
				try {
					if (act.getSchedule() != null) {
						act.getSchedule().setEndTime(
								string2calendar(dto.getEndTime()));
					} else {
						Schedule schedule = new Schedule();
						schedule.setEndTime(string2calendar(dto.getEndTime()));
						act.setSchedule(schedule);
					}
				} catch (java.text.ParseException e) {

				}
			}
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
				ActivityDTO dto = new ActivityDTO(act, false);
				dlist.add(dto);
			}
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
					ActivityDTO dto = new ActivityDTO(act, false);
					dlist.add(dto);
				}
			}

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
