package application;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Account {
	private int uid;
	private String nickName;
	private boolean enabled;

	public Account(String username, String passwd,String nickName ) {
		super();
		this.nickName = nickName;
		this.username = username;
		this.passwd = passwd;
		enabled=true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Account() {
		super();
	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	private String username;
	private String passwd;

	public int getUid() {
		return uid;
	}

	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Account(int uid) {
		super();
		this.uid = uid;
	}
	public void update(){
	      Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.update(this); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}
	public void delete(){
		Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.delete(this);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }	
	}
	public void save(){
	      Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.save(this); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }	
	}
	public static Account read(String name){
		if(name==null){
			return null;
		}
	      Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      List<Account> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("FROM Account as account where account.username = :name").setParameter("name",name).list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		return alist.size()==0?null:alist.get(0);
	}
	public static List<Account> readList(String s){
		if(s==null) s="";
	      Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      List<Account> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("FROM Account as account "+s).list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		return alist;
	}
}
