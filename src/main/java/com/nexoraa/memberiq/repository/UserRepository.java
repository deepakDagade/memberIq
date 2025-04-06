package com.nexoraa.memberiq.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID>, JpaSpecificationExecutor<AppUser> {
	AppUser findByEmailAndIsDeletedFalse(String email);

}