package application;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


import domain.Activity;
import domain.Location;
import domain.Note;

public class ActivityService {
	int user=1;

	public List<String> getNameList(){
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("ye");
		ar.add("shu");
		ar.add("hao");
		return ar;
	}

	public ActivityDTO getActivity(String aidString,int uid,boolean withNote) {
		int aid=Integer.parseInt(aidString);
		Activity act=Activity.getById(aid,uid);
		return new ActivityDTO(act,withNote);
	}
	public int addActivity(ActivityDTO dto,int uid){
		Session session = util.HibernateFactory.get().openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Activity act=new Activity();
	 		act.setName(dto.getName());
	 		act.setNote(new Note(dto.getNote()));
	 		act.setLocation(new Location(dto.getLocation()));
	 		act.setParent(Integer.parseInt(dto.getParent()));
	 		act.setUid(uid);
	 		DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	 		if(dto.getStartTime()!=null) {
	 			try{
	 				long time=df.parse(dto.getStartTime()).getTime();
	 				act.getSchedule().setStartTime(new Timestamp(time));
	 			}
	 			catch(java.text.ParseException e){
	 				
	 			}
	 		}
	 		if(dto.getEndTime()!=null) {
	 			try{
	 				long time=df.parse(dto.getEndTime()).getTime();
	 				act.getSchedule().setEndTime(new Timestamp(time));
	 			}
	 			catch(java.text.ParseException e){
	 				
	 			}
	 		}
	         session.save(act);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
			
		return 0;
	}
//	public int deleteActivity(ActivityDTO dto){
//		Activity act=new Activity();
//		act.setName(dto.getName());
//		act.setNote(new Note(dto.getNote()));
//		act.setLocation(new Location(dto.getLocation()));
//		act.setParent(new ActivityId(dto.getParent()));
//		(new ActivityDao()).insert(act, user);
//		return 0;
//	}
	
	
	//return number of rows affected
	public int editActivity(ActivityDTO dto,int uid){
		Session session = util.HibernateFactory.get().openSession();
	      Transaction tx = null;
	      List<Activity> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("from Activity act where act.aid = :aid and act.uid = :uid")
	        		 .setInteger("aid", Integer.valueOf(dto.getAid()))
	        		 .setInteger("uid", uid).list();
	         if(alist.size()==0){
	        	 return 0;
	         }
	         Activity act=alist.get(0);
	 		if(dto.getName()!=null) act.setName(dto.getName());
	 		if(dto.getNote()!=null) act.setNote(new Note(dto.getNote()));
	 		if(dto.getLocation()!=null) act.setLocation(new Location(dto.getLocation()));
	 		if(dto.getParent()!=null) act.setParent(Integer.parseInt(dto.getParent()));
	 		if(dto.getUid()!=null) act.setUid(uid);
	 		DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	 		if(dto.getStartTime()!=null) {
	 			try{
	 				long time=df.parse(dto.getStartTime()).getTime();
	 				act.getSchedule().setStartTime(new Timestamp(time));
	 			}
	 			catch(java.text.ParseException e){
	 				
	 			}
	 		}
	 		if(dto.getEndTime()!=null) {
	 			try{
	 				long time=df.parse(dto.getEndTime()).getTime();
	 				act.getSchedule().setEndTime(new Timestamp(time));
	 			}
	 			catch(java.text.ParseException e){
	 				
	 			}
	 		}
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		return 1;
	}
	public List<ActivityDTO> getChild(String aidString,int uid){
		ArrayList<ActivityDTO> dlist=new ArrayList<ActivityDTO>();
		Session session = util.HibernateFactory.get().openSession();
	      Transaction tx = null;
	      List<Activity> alist = null;
	      try{
	         tx = session.beginTransaction();
	         alist = session.createQuery("from Activity act where act.parent = :aid and act.uid = :uid")
	        		 .setInteger("aid", Integer.parseInt(aidString)).setInteger("uid", uid).list();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		for(Activity act:alist){
			ActivityDTO dto=new ActivityDTO(act,false);
			dlist.add(dto);
		}
		return dlist;
	}
	public List<ActivityDTO> getPath(String aidString,int uid){
		ArrayList<ActivityDTO> dlist=new ArrayList<ActivityDTO>();
		String curId=aidString;
		while(!curId.equals("0")){
			ActivityDTO dto = getActivity(curId,uid,false);
			if(dto.getParent()!=null){
				dlist.add(dto);
				curId=dto.getParent();
			}else{
				return new ArrayList<ActivityDTO>();
			}
		}
		Collections.reverse(dlist);
		return dlist;
	}
	
}
