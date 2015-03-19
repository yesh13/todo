package application;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
}
