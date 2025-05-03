package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nexoraa.memberiq.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, UUID>, JpaSpecificationExecutor<Expense> {

	Optional<Expense> findByIdAndOrganizationIdAndIsDeletedFalse(UUID expenseId, UUID organizationId);

	List<Expense> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

}