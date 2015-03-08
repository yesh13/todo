package application;

public class Account {
private int uid;
public Account() {
	super();
}
public void setUid(int uid) {
	this.uid = uid;
}
private String name;
private String passwd;
public int getUid() {
	return uid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPasswd() {
	return passwd;
}
public void setPasswd(String passwd) {
	this.passwd = passwd;
}
public Account(int uid) {
	super();
	this.uid = uid;
}
}
