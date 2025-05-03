package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexoraa.memberiq.entity.MembershipType;
import com.nexoraa.memberiq.enums.Status;

public interface MembershipTypeRepository
		extends JpaRepository<MembershipType, UUID>, JpaSpecificationExecutor<MembershipType> {
	Optional<MembershipType> findByNameAndOrganizationIdAndIsDeletedFalse(String name, UUID organizationId);

	List<MembershipType> findByIdInAndIsDeletedFalse(List<UUID> ids);

	@Query("SELECT m FROM MembershipType m WHERE (m.id=:id OR m.name= :name) AND m.organization.id = :organizationId AND m.isDeleted = false")
	List<MembershipType> findByIdOrNameAndOrganizationIdAndIsDeletedFalse(@Param("id") UUID id,
			@Param("name") String name, @Param("organizationId") UUID organizationId);

	List<MembershipType> findByOrganizationIdAndStatusAndIsDeletedFalse(UUID organizationId, Status active);

	List<MembershipType> findByIdInAndAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);
}