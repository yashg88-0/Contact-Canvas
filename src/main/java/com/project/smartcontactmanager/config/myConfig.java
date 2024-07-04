package com.project.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class myConfig {
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new userDetailServiceImp();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .authorizeHttpRequests((authorize) -> authorize
	    .requestMatchers("/user/**").hasAuthority("ROLE_USER").
	    requestMatchers("/admin/**").hasAnyAuthority("ADMIN").
	    requestMatchers("/**").permitAll()
            .anyRequest().authenticated()
        ).formLogin(form -> form
                .loginPage("/login")   // Specify the URL of your custom login page
                .loginProcessingUrl("/login") // URL to submit the username and password to
                .defaultSuccessUrl("/user/dashboard", true) // URL to redirect to after successful login
                .failureUrl("/login?error=true") // URL to redirect to after a failed login
                .permitAll()
            )
        .logout(logout -> logout
                .logoutUrl("/logout") // URL to trigger logout
                .logoutSuccessUrl("/login?logout=true") // URL to redirect to after successful logout
                .invalidateHttpSession(true) // Invalidate the session
                .deleteCookies("JSESSIONID") // Delete cookies
                .permitAll()
            );
		
	    return http.build();
    }
//		http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN").
//		requestMatchers("/user/**").hasRole("USER")
//		.requestMatchers("/**").permitAll().and().formLogin(withDefaults()).csrf().disable();
}