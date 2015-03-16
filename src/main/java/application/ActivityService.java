package application;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dao.ActivityDao;
import domain.Activity;
import domain.ActivityId;
import domain.Location;
import domain.Note;

public class ActivityService {
	int user=1;
	private ActivityDTO activity2Dto(Activity act,boolean withNote){
		ActivityDTO dto=new ActivityDTO();
		if(act!=null){
			dto.setAid(act.getAid().toString());
			dto.setLocation(act.getLocation().toString());
			dto.setName(act.getName());
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			if (act.getSchedule().getStartTime() != null) {
				dto.setStartTime(df.format(act.getSchedule().getStartTime()));
			}
			if (act.getSchedule().getEndTime() != null) {
				dto.setEndTime(df.format(act.getSchedule().getEndTime()));
			}
			if(withNote==true){
				dto.setNote(act.getNote().toString());	
			}
			dto.setParent(act.getParent().toString());
			}
		return dto;
	}
	public List<String> getNameList(){
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("ye");
		ar.add("shu");
		ar.add("hao");
		return ar;
	}

	public ActivityDTO getActivity(String aidString,boolean withNote) {
		Activity act=(new ActivityDao()).getById(new ActivityId(aidString),user);
		return activity2Dto(act,withNote);
	}
	public int addActivity(ActivityDTO dto){
		Activity act=new Activity();
		act.setName(dto.getName());
		act.setNote(new Note(dto.getNote()));
		act.setLocation(new Location(dto.getLocation()));
		act.setParent(new ActivityId(dto.getParent()));
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
		(new ActivityDao()).insert(act, user);
		return 0;
	}
	public int deleteActivity(ActivityDTO dto){
		Activity act=new Activity();
		act.setName(dto.getName());
		act.setNote(new Note(dto.getNote()));
		act.setLocation(new Location(dto.getLocation()));
		act.setParent(new ActivityId(dto.getParent()));
		(new ActivityDao()).insert(act, user);
		return 0;
	}
	public int editActivity(ActivityDTO dto){
		Activity act=(new ActivityDao()).getById(new ActivityId(dto.getAid()),user);
		if(dto.getName()!=null) act.setName(dto.getName());
		if(dto.getNote()!=null) act.setNote(new Note(dto.getNote()));
		if(dto.getLocation()!=null) act.setLocation(new Location(dto.getLocation()));
		if(dto.getParent()!=null) act.setParent(new ActivityId(dto.getParent()));
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
		(new ActivityDao()).update(act, user);
		return 0;
	}
	public List<ActivityDTO> getChild(String aidString){
		ArrayList<ActivityDTO> dlist=new ArrayList<ActivityDTO>();
		ArrayList<Activity> alist = (new ActivityDao()).
				getChildById(new ActivityId(aidString), user).getRealList();
		for(Activity act:alist){
			ActivityDTO dto=activity2Dto(act,false);
			dlist.add(dto);
		}
		return dlist;
	}
	public List<ActivityDTO> getPath(String aidString){
		ArrayList<ActivityDTO> dlist=new ArrayList<ActivityDTO>();
		String curId=aidString;
		while(!curId.equals("0")){
			ActivityDTO dto = getActivity(curId,false);
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
