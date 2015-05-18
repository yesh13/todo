package domain;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import util.hibernate.HibernateFactory;

public class ActivityFactory {
	public static Appointment newAppointment(int uid, int parent) {
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		Appointment act = null;
		try {
			tx = session.beginTransaction();
			act = new Appointment();
			act.setUid(uid);
			act.setParentId(parent);
			act.setStartTime(Calendar.getInstance());
			act.setFinishTime(Calendar.getInstance());
			session.save(act);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return act;
	}
	public static Task newTask(int uid, int parent) {
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		Task act = null;
		try {
			tx = session.beginTransaction();
			act = new Task();
			act.setUid(uid);
			act.setParentId(parent);
			act.setState(Task.State.Active);
			act.setStartTime(Calendar.getInstance());
			session.save(act);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return act;
	}
	public static Task newNote(int uid,int parent){
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		Task act = null;
		try {
			tx = session.beginTransaction();
			act = new Task();
			act.setUid(uid);
			act.setParentId(parent);
			act.setState(Task.State.Active);
			act.setFinishTime(Calendar.getInstance());
			session.save(act);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return act;
	}

	public static Activity getById(int aid, int uid) {
		if(aid==0){
			Activity act=new Appointment();
			act.setAid(0);
			act.setUid(uid);
			return act;
		}
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		List<Activity> alist = null;
		try {
			tx = session.beginTransaction();
			alist = session
					.createQuery(
							"from Activity act where act.aid = :aid and act.uid = :uid")
					.setInteger("aid", aid).setInteger("uid", uid).list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (alist.size() == 0) {
			return null;
		} else {
			alist.get(0).load();
			return alist.get(0);
		}
	}

}
