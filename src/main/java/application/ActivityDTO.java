package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import domain.Activity;

public class ActivityDTO {
	String aid;
	String endTime;
	String location;
	String name;
	String note;
	String parent;

	String startTime;

	String type;

	String uid;
	static public ActivityDTO getNewInstance(){
		ActivityDTO dto=new ActivityDTO();
		dto.setType("0");
		return dto;
	}
	public ActivityDTO() {
		// TODO Auto-generated constructor stub
	}

	public ActivityDTO(Activity act, boolean withNote) {
		if (act != null) {
			setAid(String.valueOf(act.getAid()));
			setLocation(act.getLocation().toString());
			setName(act.getName());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			if (act.getSchedule() != null) {
				if (act.getSchedule().getStartTime() != null) {
					setStartTime(df.format(act.getSchedule().getStartTime().getTime()));
				}
				if (act.getSchedule().getEndTime() != null) {
					setEndTime(df.format(act.getSchedule().getEndTime().getTime()));
				}
			}
			if (withNote == true) {
				setNote(act.getNote().toString());
			}
			setParent(String.valueOf(act.getParent()));
			setType(String.valueOf(act.getType()));
		}
	}

	public String getAid() {
		return aid;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}
	public String getParent() {
		return parent;
	}

	public String getStartTime() {
		return startTime;
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

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


}
