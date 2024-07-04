package com.project.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.smartcontactmanager.Dao.DaoJPA;
import com.project.smartcontactmanager.entities.LoginData;
import com.project.smartcontactmanager.entities.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private DaoJPA userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping("/")
	public String about() {
		return "home";
	}

	@GetMapping("/form")
	public String form(Model model) {
		System.out.println("form is running");
		model.addAttribute("loginData", new LoginData());
		return "form";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Registration - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/process")
	public String processForm(@Valid @ModelAttribute("loginData") LoginData loginData, BindingResult result) {
		if (result.hasErrors()) {
			return "form";
		}
		System.out.println(loginData.toString());
		return "success";
	}

	@PostMapping("/do_register")
	public String registerUser(@ModelAttribute("user") User user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email, 
			@RequestParam(value = "password", required = false) String password, 
			Model model) {
		// check the size of username
		if (name.length() <= 3 || name.length() >= 20) {
			model.addAttribute("name", "Name must be between size of 3 and 20!!");
			return "signup";
		}
		//check if password is not empty
		if (password.isBlank()) {
			model.addAttribute("password", "Password cannot be empty!!");
			return "signup";
		}
		// check if email is already existing or not
		if (!email.isBlank()) {
			User userByUserName = userRepository.getUserByUserName(email);
			if (userByUserName != null) {
				model.addAttribute("email", "Email is already in use!!");
				return "signup";
			}
		}
		// check if agreement is selected or not
		if (!agreement) {
			model.addAttribute("agreement", "Please check the agreement!!");
			return "signup";
		}
		// set the user_details on their details
		user.setEnabled(true);
		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		model.addAttribute("success", "Successfully Registered!!");
		return "signup";
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		// Invalidate JWT tokens or handle session invalidation
		// Example assumes that JWT tokens are handled via cookies

		// Clear the JWT token from the response cookies
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("Authorization")) {
					// Invalidate the cookie
					cookie.setValue(null);
					cookie.setMaxAge(0);
					cookie.setPath("/"); // Ensure the path is the same as the one used when setting the cookie
					response.addCookie(cookie);
					break;
				}
			}
		}

		// Clear security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}

		// Optionally, you can also handle additional custom cleanup logic here
		// Respond with a success message
		String redirectUrl = "/login?logout=true";
		return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
	}

	@GetMapping("/login")
	public String customLogin(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "missingField", required = false) String missingField,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null)
			model.addAttribute("error", "Invalid Email Address or Password!!");

		if (missingField != null)
			model.addAttribute("missingField", "Username and password are required!!");

		if (logout != null)
			model.addAttribute("logout", "You have logged out successfully!!");

		return "login";
	}
//Spring Security will auto-handle this
//	@PostMapping("/signin")
//    public String handleLogin(@ModelAttribute("user") User user, 
//    						  @RequestParam("email") String username, 
//                              @RequestParam("password") String password,
//                              @RequestParam(value = "logout", required = false) String logout,
//                              RedirectAttributes redirectAttributes,
//                              Model model) {
//        if (username.isEmpty() || password.isEmpty()) {
//            redirectAttributes.addAttribute("missingField", true);
//            System.out.println("First block");
//            return "redirect:/signin";
//        }
//        if (logout != null) {
//			model.addAttribute("logout", "You have logged out successfully!!");
//			System.out.println("logout block is triggered");
//			return "signin";
//        }
//        User userByUserName = userRepository.getUserByUserName(username);
//        
//        if (userByUserName == null || !bCryptPasswordEncoder.matches(password, userByUserName.getPassword())){
//            System.out.println(bCryptPasswordEncoder.matches(password, userByUserName.getPassword())+"Passowrd is not matching");
//            redirectAttributes.addAttribute("error", true);
//            System.out.println("Second block");
//            return "redirect:/signin";
//        }
//        System.out.println("Third block");
//        return "redirect:/user/dashboard";
//	}
}
