package domain;

public class Activity {
	private ActivityId aid;
	private ActivitySet child;
	private Location location=new Location("");
	private String name;
	private Note note=new Note("");
	private ActivityId parent;

	public Activity() {
		super();
	}

	public Activity(ActivityId aid) {
		super();
		this.aid = aid;
	}

	public ActivityId getAid() {
		return aid;
	}

	public ActivitySet getChild() {
		return child;
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Note getNote() {
		return note;
	}

	public ActivityId getParent() {
		return parent;
	}

	public void setChild(ActivitySet child) {
		this.child = child;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public void setParent(ActivityId parent) {
		this.parent = parent;
	}
}
