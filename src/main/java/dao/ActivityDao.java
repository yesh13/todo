package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import util.MybatisFactory;
import domain.Activity;

public class ActivityDao {
	public Activity getById(int aid,int uid){		
		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		  Activity act=mapper.getById(aid,uid);
		  return act;
		}
		finally {
		  session.close();
		}
	}
	public int insert(Activity act,int uid){
		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
			mapper = session.getMapper(ActivityMapper.class);
			return mapper.addActivity(uid,act);
		}
		finally {
			session.commit();
			session.close();
		}
		
	}
	public int update(Activity act, int uid) {
		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		  return mapper.updateActivity(uid,act);
		}
		finally {
		session.commit();
		  session.close();
		}
	}

}
