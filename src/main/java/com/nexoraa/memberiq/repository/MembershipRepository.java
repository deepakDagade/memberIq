package com.nexoraa.memberiq.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID>, JpaSpecificationExecutor<Membership> {

	Optional<Membership> findByIdAndOrganizationIdAndIsDeletedFalse(UUID id, UUID organizationId);

	List<Membership> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

	List<Membership> findByOrganizationIdAndIsDeletedFalse(UUID organizationId);

	@Query("SELECT COUNT(m) " +
		       "FROM Membership m " +
		       "WHERE m.organization.id = :organizationId " +
		       "AND m.endDate = :date " +
		       "AND m.isDeleted = false")
		Long countMembershipsByEndDate(
		        @Param("organizationId") UUID organizationId,
		        @Param("date") LocalDate date);
	
}