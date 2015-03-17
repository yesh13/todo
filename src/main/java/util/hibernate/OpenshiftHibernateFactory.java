package util.hibernate;


import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class OpenshiftHibernateFactory extends HibernateFactory{
	private SessionFactory factory=null;
	
	public SessionFactory buildSessionFactory(){
		if(factory==null){
			Properties prop=new Properties();
			String url="jdbc:mysql://"+System.getenv("OPENSHIFT_MYSQL_DB_HOST")+":"+System.getenv("OPENSHIFT_MYSQL_DB_PORT")+
					"/todo?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";
			prop.setProperty("hibernate.connection.url",url);
			prop.setProperty("hibernate.connection.username", System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"));
			prop.setProperty("hibernate.connection.password", System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD"));
		      try{
		         factory = new Configuration().configure()
		        		 .setProperties(prop).buildSessionFactory();
		      }catch (Throwable ex) { 
		         System.err.println("Failed to create sessionFactory object." + ex);
		         throw new ExceptionInInitializerError(ex); 
		      }
		}
		return null;
	}
}
