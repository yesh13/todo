package util.hibernate;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class LocalHibernateFactory extends HibernateFactory{
	private SessionFactory factory=null;

	public SessionFactory buildSessionFactory(){
		if(factory==null){
			Properties prop=new Properties();
			prop.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/todo?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull");
			prop.setProperty("hibernate.connection.username", "todo");
			prop.setProperty("hibernate.connection.password", "1520");
		      try{
		         factory = new Configuration().configure().buildSessionFactory();
		      }catch (Throwable ex) { 
		         System.err.println("Failed to create sessionFactory object." + ex);
		         throw new ExceptionInInitializerError(ex); 
		      }
		}
		return factory;
	}
}
