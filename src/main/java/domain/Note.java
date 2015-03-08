package domain;

public class Note {
	private String note;

	public Note() {
		super();
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return note;
	}

	public Note(String note) {
		super();
		this.note = note;
	}
	
}
