package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID>, JpaSpecificationExecutor<Group> {

	List<Group> findByOrganizationIdAndIsDeletedFalse(UUID organizationId);

	Optional<Group> findByNameAndOrganizationIdAndIsDeletedFalse(String name, UUID orgId);

	@Query("SELECT g FROM Group g WHERE (g.id=:id OR g.name= :name) AND g.organization.id = :organizationId AND g.isDeleted = false")
	List<Group> findByIdOrNameAndOrganizationIdAndIsDeletedFalse(@Param("id") UUID id, @Param("name") String name,
			@Param("organizationId") UUID organizationId);

	List<Group> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID id);
}