package domain;
import java.util.ArrayList;

public class Today {
	ArrayList<String> acts=new ArrayList<String>();
	public ArrayList<String> getActs() {
		return acts;
	}
	public void setActs(ArrayList<String> acts) {
		this.acts = acts;
	}
	public Today(){
		acts.add("Getup");
		acts.add("work");
		acts.add("Sleep");
	}
}
