package com.project.smartcontactmanager.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.smartcontactmanager.entities.Contact;
import com.project.smartcontactmanager.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	// pagination
	@Query("from Contact as C where C.user.id=:userId")
	// current-page and No. of contacts per page
	public Page<Contact> getContactListByUser(@Param("userId") int id, Pageable pageable);
	
	@Query("from Contact as C where C.user.id=:userId")
	// current-page and No. of contacts per page
	public List<Contact> getContactListByUser(@Param("userId") int id);

	/*
	 * Searching Contacts Hibernate JPA will provide custom method just by giving
	 * specialized name findByNameContainingAndUser
	 */
	//List<Contact> findByContact_nameContainingAndUser(String contact_name, User user);
	//But the above method is not working as I am using contact_name
	
	@Query("SELECT c FROM Contact c WHERE c.contact_name LIKE %:query% AND c.user = :user")
    List<Contact> findByContactNameContainingAndUser(@Param("query") String query, @Param("user") User user);
}
