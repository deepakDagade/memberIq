package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.ExpenseCategory;
import com.nexoraa.memberiq.enums.Status;

@Repository
public interface ExpenseCategoryRepository
		extends JpaRepository<ExpenseCategory, UUID>, JpaSpecificationExecutor<ExpenseCategory> {

	Optional<ExpenseCategory> findByNameAndOrganizationIdAndIsDeletedFalse(String name, UUID organizationId);

	List<ExpenseCategory> findByIdOrNameAndOrganizationIdAndIsDeletedFalse(UUID id, String name, UUID organizationId);

	List<ExpenseCategory> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

	List<ExpenseCategory> findByOrganizationIdAndStatusAndIsDeletedFalse(UUID organizationId,Status active);
}