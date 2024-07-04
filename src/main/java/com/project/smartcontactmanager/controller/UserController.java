package com.project.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.smartcontactmanager.Dao.ContactRepository;
import com.project.smartcontactmanager.Dao.DaoJPA;
import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;
import com.project.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private DaoJPA userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void commonDataMethod(Model model, Principal principal) {
		String name = principal.getName();
		User userByUserName = userRepository.getUserByUserName(name);
		model.addAttribute("user", userByUserName);
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Dashboard");
		return "User/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String addContactDetails(Model model, Principal principal) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "User/add_contact";
	}

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile multipartFile, Principal principal, Model model) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			contact.setUser(user);

			// Upload file in image column
			if (multipartFile.isEmpty()) {
				System.out.println("File is empty");
				contact.setImage("person.png");
			} else {
				contact.setImage(multipartFile.getOriginalFilename());
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths
						.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			contactRepository.save(contact);//need to first save the contact before user
			user.getContactList().add(contact);
			System.out.println(user.getName() + " " + user.getRole() + " " + user.getContactList());
			userRepository.save(user);
			System.out.println(contact.toString());

			// success message
			model.addAttribute("message", new Message("Your contact is successfully added!!", "success"));

		} catch (Exception e) {
			// error message
			e.printStackTrace();
			model.addAttribute("message", new Message("Something went wrong" + e.getMessage(), "danger"));
		}
		return "User/add_contact";
	}

	// show contacts
	// per-page = n = 3
	// current-page = page = 0
	@GetMapping("/show-contacts/{page}")
	public String show_contacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show Contact");
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);

		// current-page and No of contacts per page
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contactListByUser = contactRepository.getContactListByUser(user.getId(), pageable);

		model.addAttribute("contacts", contactListByUser);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contactListByUser.getTotalPages());
		model.addAttribute("user", user);

		return "User/show_contacts";
	}

	// show individual contacts
	@GetMapping("/contact/{cId}")
	public String show_individual_contacts(@PathVariable("cId") Integer cId, 
			Model model, Principal principal) {
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();//get the contact details
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);//to get the user
		
		//No other user access the contacts details
		if(user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);//render the contact on page
		
		return "User/show_individual_contact";
	}
	
	//delete individual contacts
	@GetMapping("/delete-contact/{ID}")
	public String deleteContact(@PathVariable("ID") Integer id, 
			Model model, Principal principal) {
		System.out.println(id);
		Contact contact = contactRepository.findById(id).get();//get the contact details
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);//to get the user
		
		if(user.getId() == contact.getUser().getId()) {
			contact.setUser(null);//first de-link the user
			System.out.println(contact.toString());
			contactRepository.delete(contact);
		}
			
		model.addAttribute("deleted", "is deleted successfully");
		return "redirect:/user/show-contacts/0";
	}
}
