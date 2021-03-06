package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import util.MybatisFactory;
import domain.Activity;
import domain.ActivityId;
import domain.ActivitySet;

public class ActivityDao {
	public Activity getById(ActivityId aid,int uid){		
		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		  Activity act=mapper.getById(aid.mainId(),uid);
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
	public ActivitySet getChildById(ActivityId aid,int uid){
		SqlSession session = MybatisFactory.get().openSession();
		ActivityMapper mapper;
		try {
		  mapper = session.getMapper(ActivityMapper.class);
		List<Activity> list=mapper.getRealChildById(uid, aid.mainId());
		  return (new ActivitySet()).setRealList(list);
		}
		finally {
		session.commit();
		  session.close();
		}
	}
}
