package util.hibernate;


import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class OpenshiftHibernateFactory extends HibernateFactory{
	private SessionFactory factory=null;
	
	@SuppressWarnings("deprecation")
	public SessionFactory buildSessionFactory(){
		if(factory==null){
			Properties prop=new Properties();
			String url="jdbc:mysql://"+System.getenv("OPENSHIFT_MYSQL_DB_HOST")+":"+System.getenv("OPENSHIFT_MYSQL_DB_PORT")+
					"/todo?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";
			prop.setProperty("hibernate.connection.url",url);
			prop.setProperty("hibernate.connection.username", System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"));
			prop.setProperty("hibernate.connection.password", System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD"));
		      try{
		         factory = new Configuration().setProperties(prop).configure()
		        		 .buildSessionFactory();
		      }catch (Throwable ex) { 
		    	  ex.printStackTrace();
			         throw ex;  
		      }
		}
		return factory;
	}
}
