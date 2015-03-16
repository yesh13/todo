package daoTest;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import util.MybatisFactory;
import dao.ActivityMapper;
import domain.Activity;
import domain.ActivityId;
public class ActivityMapperTest {
	@Test
	public void add(){

		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
//		  return mapper.insert(uid,act.getName(),act.getParent().toString(),
//				  act.getLocation().toString(),act.getNote().toString());
		  Activity act=new Activity(new ActivityId("1"));
		  act.setName("ActivityMapperTest");
		  act.setParent(new ActivityId("0"));
		  //mapper.addActivity(1, act);
		}
		finally {
			  session.commit();
		  session.close();
		}
	}
	@Test
	public void get(){

		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		  ;
		  org.junit.Assert.assertEquals("get",mapper.getById(1, 1).getName(), "ActivityMapperTest");
		}
		finally {
		  session.close();
		}
	}
	@Test
	public void getChild(){

		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		  List<Activity> size = mapper.getRealChildById(1, 0);
		  org.junit.Assert.assertEquals("child", size.size(), 5);
		}
		finally {
		  session.close();
		}
	}
	
}
