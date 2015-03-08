package daoTest;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import util.MybatisFactory;
import dao.ActivityMapper;
import domain.Activity;
public class ActivityMapperTest {
	@Test
	public void add(){

		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
//		  return mapper.insert(uid,act.getName(),act.getParent().toString(),
//				  act.getLocation().toString(),act.getNote().toString());
		  mapper.addActivity(1, "11", 22, "loc", "note");
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
		  mapper.getById(1, 1);
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
		  List<Activity> size = mapper.getRealChildById(0, 1);
		  org.junit.Assert.assertEquals("child", size.size(), 5);
		}
		finally {
		  session.close();
		}
	}
	
}
