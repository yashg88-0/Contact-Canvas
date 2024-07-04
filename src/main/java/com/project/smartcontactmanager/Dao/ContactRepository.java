package com.project.smartcontactmanager.Dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.smartcontactmanager.entities.Contact;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	//pagination
	@Query("from Contact as C where C.user.id=:userId")
	//current-page and No. of contacts per page
	public Page<Contact> getContactListByUser(@Param("userId") int id, Pageable pageable);
}
