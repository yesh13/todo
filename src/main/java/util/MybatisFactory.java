package util;

import java.io.IOException;
import java.io.InputStream;

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
			factory = new SqlSessionFactoryBuilder().build(inputStream);
		}
		return factory;
	}
}
