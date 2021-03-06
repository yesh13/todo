package util.hibernate;


import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class LocalHibernateFactory extends HibernateFactory{
	private SessionFactory factory=null;

	@SuppressWarnings("deprecation")
	public SessionFactory buildSessionFactory(){
		if(factory==null){
			Properties prop=new Properties();
			prop.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/todo?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull");
			prop.setProperty("hibernate.connection.username", "todo");
			prop.setProperty("hibernate.connection.password", "1520");
		      try{
		         factory = new Configuration().setProperties(prop).configure().buildSessionFactory();
		      }catch (HibernateException ex) { 
		         ex.printStackTrace();
		         throw ex; 
		      }
		}
		return factory;
	}
}
