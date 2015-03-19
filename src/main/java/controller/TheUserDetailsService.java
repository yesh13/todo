package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import application.Account;

public class TheUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		application.Account user = Account.read(name);
		if(user==null){
			throw new UsernameNotFoundException(name);
			
		}else{
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	 
			return new User(user.getUsername(), 
					user.getPasswd(), user.isEnabled(), 
	                true, true, true, authorities);
			
		}
	}

}
