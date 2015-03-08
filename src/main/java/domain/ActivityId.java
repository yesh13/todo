package domain;

public class ActivityId {
	private int id;
	public String toString(){
		return String.valueOf(id);
	}
	public ActivityId(String id){
		if(id==null) {
			this.id=0;return;
		}
		this.id=Integer.parseInt(id);
	}
	public ActivityId(java.lang.Integer id){
		this.id=id;
	}
	public int mainId(){
		return id;
	}
}
