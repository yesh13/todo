package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import domain.Activity;

public class ActivityDTO {
	String name;
	String location;
	String note;
	String aid;
	String parent;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	String uid;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	String startTime;
	String endTime;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
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
		}
	}

	public ActivityDTO() {
		// TODO Auto-generated constructor stub
	}


}
