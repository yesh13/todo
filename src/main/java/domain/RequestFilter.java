package domain;

import java.util.Calendar;

public class RequestFilter {
		private Calendar t1;
		private Calendar t2;
		private int sub;
		public RequestFilter(){
			t1=Calendar.getInstance();
			t2=Calendar.getInstance();
			t1.set(Calendar.YEAR, 1970);
			t2.set(Calendar.YEAR, 2970);
			sub=15;
		}
		public boolean allowTask(){
			return (sub&2)!=0;
		}
		public boolean allowAppt(){
			return (sub&1)!=0;
		}
		public boolean allowNote(){
			return (sub&4)!=0;
		}
		public boolean allowPend(){
			return (sub&8)!=0;
		}
		public Calendar getT1() {
			return t1;
		}
		public void setT1(Calendar t1) {
			this.t1 = t1;
		}
		public Calendar getT2() {
			return t2;
		}
		public void setT2(Calendar t2) {
			this.t2 = t2;
		}
		public int getSub() {
			return sub;
		}
		public void setSub(int sub) {
			this.sub = sub;
		}
}
