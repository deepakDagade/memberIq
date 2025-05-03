package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID>, JpaSpecificationExecutor<Profile> {

	Optional<Profile> findByPhoneNumberAndOrganizationIdAndIsDeletedFalse(String phoneNumber, UUID organizationId);

	List<Profile> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

	@Query("SELECT p FROM Profile p WHERE (p.id=:id OR p.phoneNumber= :phoneNumber) AND p.organization.id = :organizationId AND p.isDeleted = false")
	List<Profile> findByIdOrPhoneNumberAndOrganizationIdAndIsDeletedFalse(@Param("id") UUID id,
			@Param("phoneNumber") String phoneNumber, @Param("organizationId") UUID organizationId);

	List<Profile> findByOrganizationIdAndIsDeletedFalse(UUID id);

}