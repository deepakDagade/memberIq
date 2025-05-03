package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID>, JpaSpecificationExecutor<AppUser> {
	AppUser findByEmailAndIsDeletedFalse(String email);

	AppUser findByEmail(String email);

	Optional<AppUser> findByEmailAndOrganizationIdAndIsDeletedFalse(String email, UUID organizationId);

	Page<AppUser> findByOrganizationIdAndIsDeletedFalse(UUID orgId, Pageable pageable);

	List<AppUser> findByIdIn(List<UUID> userIds);

	AppUser findByResetPasswordTokenAndIsDeletedFalse(String token);

}