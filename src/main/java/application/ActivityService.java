package application;

import java.util.ArrayList;
import java.util.List;

import dao.ActivityDao;
import domain.Activity;
import domain.ActivityId;
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

	public ActivityDTO getActivity(String aidString) {
		ActivityDTO dto=new ActivityDTO();
		Activity act=(new ActivityDao()).getById(new ActivityId(aidString),user);
		dto.setAid(aidString);
		dto.setLocation(act.getLocation().toString());
		dto.setName(act.getName());
		dto.setNote(act.getNote().toString());
		dto.setParent(act.getParent().toString());
		return dto;
	}
	public int insertActivity(ActivityDTO dto){
		Activity act=new Activity();
		act.setName(dto.getName());
		act.setNote(new Note(dto.getNote()));
		act.setLocation(new Location(dto.getLocation()));
		act.setParent(new ActivityId(dto.getParent()));
		(new ActivityDao()).insert(act, user);
		return 0;
	}
	public int editActivity(ActivityDTO dto){
		if(dto.getAid()==null) return -1;
		Activity act=(new ActivityDao()).getById(new ActivityId(dto.getAid()),user);
		if(dto.getName()!=null) act.setName(dto.getName());
		if(dto.getNote()!=null) act.setNote(new Note(dto.getNote()));
		if(dto.getLocation()!=null) act.setLocation(new Location(dto.getLocation()));
		if(dto.getParent()!=null) act.setParent(new ActivityId(dto.getParent()));
		(new ActivityDao()).update(act, user);
		return 0;
	}
	public List<ActivityDTO> getChild(String aidString){
		ArrayList<ActivityDTO> dlist=new ArrayList<ActivityDTO>();
		ArrayList<Activity> alist = (new ActivityDao()).
				getChildById(new ActivityId(aidString), user).getRealList();
		for(Activity act:alist){
			ActivityDTO dto=new ActivityDTO();
			dto.setAid(aidString);
			dto.setLocation(act.getLocation().toString());
			dto.setName(act.getName());
			dto.setNote(act.getNote().toString());
			dto.setParent(act.getParent().toString());
			dlist.add(dto);
		}
		return dlist;
	}
	
}
