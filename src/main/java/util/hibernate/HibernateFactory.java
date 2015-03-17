package util.hibernate;


import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

abstract public class HibernateFactory {
	public static HibernateFactory factory=null;
	public static HibernateFactory getInstance(){
		if(factory!=null){
			return factory;
		}
		String host=System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		if(host!=null){
			factory=new OpenshiftHibernateFactory();
		}else{
			factory=new LocalHibernateFactory();
			
		}
		return factory;
	}
	abstract public SessionFactory buildSessionFactory();
}
