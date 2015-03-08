package dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import util.MybatisFactory;
import application.Account;
import domain.Activity;
import domain.ActivityId;
import domain.ActivitySet;
import domain.Location;
import domain.Note;

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
			return mapper.addActivity(uid,act.getName(),act.getParent().mainId(),
				  act.getLocation().toString(),act.getNote().toString());
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
		  return mapper.updateActivity(act.getAid().mainId(),uid,act.getName(),act.getParent().mainId(),
				  act.getLocation().toString(),act.getNote().toString());
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
		List<Activity> list=mapper.getRealChildById(aid.mainId(), uid);
		  return (new ActivitySet()).setRealList(list);
		}
		finally {
		session.commit();
		  session.close();
		}
	}
}
