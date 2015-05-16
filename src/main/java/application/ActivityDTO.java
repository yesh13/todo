package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import domain.Activity;
import domain.Schedule;

public class ActivityDTO {
	class BasicActivityDTO {
		String aid;
		String title;
	}

	String aid;
	String title;
	String description;
	String parent;

	String type;
	String uid;
	String priority;
	Schedule schedule;
	ArrayList<BasicActivityDTO> subacts;


	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	static public ActivityDTO getNewInstance() {
		ActivityDTO dto = new ActivityDTO();
		dto.setType("0");
		return dto;
	}

	public ActivityDTO() {
		// TODO Auto-generated constructor stub
	}

	public ActivityDTO(Activity act, boolean withNote) {
		if (act != null) {
			setAid(String.valueOf(act.getAid()));
			setTitle(act.getName());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			setSchedule(act);
			if (withNote == true) {
				setDescription(act.getDescription());
			}
			setParent(String.valueOf(act.getParentId()));
			setType(String.valueOf(act.getType()));
			setPriority(String.valueOf(act.getPriority()));
		}
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public String getAid() {
		return aid;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<BasicActivityDTO> getSubacts() {
		return subacts;
	}

	public void setSubacts(ArrayList<BasicActivityDTO> subacts) {
		this.subacts = subacts;
	}

	public String getTitle() {
		return title;
	}

	public String getParent() {
		return parent;
	}


	public String getType() {
		return type;
	}

	public String getUid() {
		return uid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}


	public void setTitle(String name) {
		this.title = name;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}



	public void setType(String type) {
		this.type = type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
