package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.Organization;

@Repository
public interface OrganizationRepository
		extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {

	Organization findByNameAndIsDeletedFalse(String name);

	@Query("SELECT o FROM Organization o WHERE (o.id=:id OR o.name= :name) AND o.isDeleted = false")
	List<Organization> findByIdOrNameAndIsDeletedFalse(@Param("id") UUID id, @Param("name") String name);

	Optional<Organization> findByIdAndIsDeletedFalse(UUID id);

}
