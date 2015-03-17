package application;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Account {
	private int uid;
	private String nickName;

	public Account() {
		super();
	}

	public Account(String name, String nickName, String passwd) {
		this.name = name;
		this.nickName = nickName;
		this.passwd = passwd;
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

	private String name;
	private String passwd;

	public int getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public void create(){
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
		String hql="where Account.name = "+name;
		List<Account> alist=readList(hql);
		return alist.size()==0?null:alist.get(0);
	}
	public static List<Account> readList(String s){
		if(s==null) s="";
	      Session session = util.hibernate.HibernateFactory.getInstance().buildSessionFactory().openSession();
	      Transaction tx = null;
	      List<Account> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("FROM Account "+s).list();
	         alist.get(0).setName("yujingxiang");
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
