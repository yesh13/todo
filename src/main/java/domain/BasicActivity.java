package domain;

import javax.persistence.PostLoad;

public class BasicActivity {
	private boolean loaded=false;
	@PostLoad
	public void load(){
		loaded=true;
	}
	
	private int uid;
	private int aid;
	private String name;
	private int parentId;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		if(!loaded)
		this.uid = uid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		if(!loaded)
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		if(!loaded)
		this.parentId = parentId;
	}
}
