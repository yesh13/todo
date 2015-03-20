package daoTest;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import application.Account;

public class HBMAccount {
	   private static SessionFactory factory; 
	   public static void main(String[] args) {
	      /* Add few employee records in database */
//		      Account a1=(new Account("Zara", "Ali", "ssd"));
//		      a1.create();
//		      Account a2=new Account("Daisy", "Das", "ss");
//		      a2.create();
//		      Account a3=new Account("John", "Paul", "yeshuhao");
//		      a3.create();

	      /* List down all the employees */
	      Account.readList(null);

	      /* Update employee's records */
//	      a1.setNickName("Shuhao");
//	      a1.update();
//
//	      /* Delete an employee from the database */
//	      a2.delete();

	      /* List down new list of the employees */
	      Account.readList(null);
	   }
	   /* Method to CREATE an employee in the database */
	   public Integer addAccount(String fname, String lname, String salary){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      Integer employeeID = null;
	      try{
	         tx = session.beginTransaction();
	         Account employee = new Account(fname, lname, salary);
	         employeeID = (Integer) session.save(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return employeeID;
	   }
	   /* Method to  READ all the employees */
	   public void listAccounts( ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         List employees = session.createQuery("FROM Account").list(); 
	         for (Iterator iterator = 
	                           employees.iterator(); iterator.hasNext();){
	            Account employee = (Account) iterator.next(); 
	            System.out.print("Name: " + employee.getName()); 
	            System.out.print(" Nick Name: " + employee.getNickName()); 
	            System.out.println(" Passwd: " + employee.getUid()); 
	         }
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	   /* Method to UPDATE salary for an employee */
	   public void updateAccount(Integer AccountID, String salary ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Account employee = 
	                    (Account)session.get(Account.class, AccountID); 
	         employee.setPasswd(salary);
			 session.update(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	   /* Method to DELETE an employee from the records */
	   public void deleteAccount(Integer AccountID){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Account employee = 
	                   (Account)session.get(Account.class, AccountID); 
	         session.delete(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
}