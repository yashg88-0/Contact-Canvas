package com.project.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.smartcontactmanager.Dao.ContactRepository;
import com.project.smartcontactmanager.Dao.DaoJPA;
import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;

@RestController
public class SearchController {
	
	@Autowired
	private DaoJPA userRepository;
	@Autowired
	private ContactRepository contactRepository;

	// search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, 
			Model model, Principal principal) {
		//get the user
		User user = userRepository.getUserByUserName(principal.getName());
		
		System.out.println(query);
		List<Contact> contacts = contactRepository.findByContactNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
