package domain;

public class Location {
	private String place;

	@Override
	public String toString() {
		return place;
	}

	public Location(String place) {
		super();
		if(place==null){
			this.place="";
		}else{
			this.place = place;
		}
	}

	public String getPlace() {
		return place;
	}

	public Location() {
		super();
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
