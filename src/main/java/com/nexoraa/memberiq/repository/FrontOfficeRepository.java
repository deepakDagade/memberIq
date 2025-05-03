package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.FrontOffice;

@Repository
public interface FrontOfficeRepository extends JpaRepository<FrontOffice, UUID>, JpaSpecificationExecutor<FrontOffice> {

	Optional<FrontOffice> findByIdAndOrganizationIdAndIsDeletedFalse(UUID id, UUID organizationId);

	List<FrontOffice> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

	List<FrontOffice> findByOrganizationIdAndIsDeletedFalse(UUID organizationId);

}
