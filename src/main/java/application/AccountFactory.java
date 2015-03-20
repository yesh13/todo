package application;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import controller.UserWithId;

public class AccountFactory {
	public Account getUser(){
		String username=getUsername();
		return Account.read(username);
	}
    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("anonymousUser")){
            return auth.getName();}
        else{
        	return null;
        }
    }
    public int getUid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("anonymousUser")){
            UserWithId user=((UserWithId) auth.getPrincipal());
            return user.getUserID();
        }
        else{
        	return 1;
        }
    }
}
