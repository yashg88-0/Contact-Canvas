package com.project.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.smartcontactmanager.Dao.DaoJPA;
import com.project.smartcontactmanager.entities.User;

public class userDetailServiceImp implements UserDetailsService{
	
	@Autowired
	private DaoJPA userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from database
		User userByUserName = userRepository.getUserByUserName(username);
		
		if(userByUserName == null)
			throw new UsernameNotFoundException(username);
		customUserDetail cud = new customUserDetail(userByUserName);
		return cud;
	}

}
