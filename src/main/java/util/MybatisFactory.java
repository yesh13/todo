package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

public class MybatisFactory {
	private static SqlSessionFactory factory=null;
	public static SqlSessionFactory get(){
		if(factory==null){
			String resource = "util/mybatis-config.xml";
			InputStream inputStream=null;
			try {
				inputStream = Resources.getResourceAsStream(resource);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String host=System.getenv("OPENSHIFT_MYSQL_DB_HOST");
			if(host!=null){
				Properties prop=new Properties();
				prop.setProperty("host", host);
				prop.setProperty("port", System.getenv("OPENSHIFT_MYSQL_DB_PORT"));
				prop.setProperty("username", System.getenv("OPENSHIFT_MYSQL_DB_USERNAME"));
				prop.setProperty("passwd", System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD"));
				factory = new SqlSessionFactoryBuilder().build(inputStream, "openshift", prop);
			}else{
				factory = new SqlSessionFactoryBuilder().build(inputStream,"development");
				
			}
		}
		return factory;
	}
}
