package domain;
import java.util.ArrayList;
public class Todo {
	ArrayList<String> acts=new ArrayList<String>();
	public ArrayList<String> getActs() {
		return acts;
	}
	public void setActs(ArrayList<String> acts) {
		this.acts = acts;
	}
	public void addAct(String name){
		acts.add(name);
	}
	public void rmAct(String name){
		acts.remove(name);
	}
	private Todo(){
		acts.add("Getup");
		acts.add("work");
		acts.add("Sleep");
	}
	static Todo inst=new Todo();
	static public Todo get(){return inst;}
}
