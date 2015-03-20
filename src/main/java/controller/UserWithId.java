package controller;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserWithId extends User{
	private final int userID;




	public UserWithId(String username, String password, boolean enabled, boolean accountNonExpired,
	                  boolean credentialsNonExpired,
	                  boolean accountNonLocked,
	                  Collection<? extends GrantedAuthority> authorities, int userID)
	{
	    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	    this.userID = userID;
	}




	public int getUserID() {
		return userID;
	}
}
