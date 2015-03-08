package domain;
import java.util.ArrayList;
import java.util.List;
public class ActivitySet {
	private ArrayList<Activity> realList;
	public ArrayList<Activity> getRealList() {
		return realList;
	}
	public ActivitySet setRealList(List<Activity> list) {
		this.realList = (ArrayList<Activity>) list;
		return this;
	}
	public ActivitySet(){
		
	}
	public int getSize(){
		return realList.size();
	}
}
