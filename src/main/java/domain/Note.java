package domain;

public class Note {
	private String note;

	public Note() {
		super();
	}

	public String getNote() {
		return note;
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
		if(note==null){
			this.note="";
		}else{
			this.note = note;
		}
	}
	
}
