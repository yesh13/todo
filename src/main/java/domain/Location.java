package domain;

public class Location {
	private String place;

	@Override
	public String toString() {
		return place;
	}

	public Location(String place) {
		super();
		this.place = place;
	}

	public Location() {
		super();
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
