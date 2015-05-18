package domain;

import javax.persistence.PostLoad;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import application.Account;
import application.AccountFactory;

public class BasicActivity {
	private boolean loaded=false;
	@PostLoad
	public void load(){
		loaded=true;
	}
	
	private int uid;
	private int aid;
	private String name;
	private int parentId;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		if(!loaded)
		this.uid = uid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		if(!loaded)
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		if(!loaded)
		this.parentId = parentId;
	}
	public int removeSelf(){
		int ret=0;
		if(getAid()<100)return 1;
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			parentId=1;
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
}
