package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Expense;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.ExpenseRepository;
import com.nexoraa.memberiq.specification.ExpenseSpecification;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

	private final ExpenseRepository expenseRepository;

	public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}

	@Override
	public Expense save(Expense expense) {
		Organization organization = UtilityService.getUserOrganization();
		expense.setOrganization(organization);
		expense.setIsDeleted(false);
		return expenseRepository.save(expense);
	}

	@Override
	public Expense update(Expense expense) {
		// Validate if expense exists
		Expense existingExpense = validateExpenseExists(expense.getId(), expense.getOrganization().getId());

		existingExpense = copyFields(existingExpense, expense);

		return expenseRepository.save(existingExpense);
	}

	private Expense copyFields(Expense existingExpense, Expense expense) {
		existingExpense.setAmount(expense.getAmount());
		existingExpense.setExpenseDate(expense.getExpenseDate());
		existingExpense.setDescription(expense.getDescription());
		existingExpense.setReceiptUrl(expense.getReceiptUrl());
		existingExpense.setRecurring(expense.isRecurring());
		existingExpense.setStatus(expense.getStatus());
		return existingExpense;
	}

	private Expense validateExpenseExists(UUID id, UUID organizationId) {
		return expenseRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, organizationId)
				.orElseThrow(() -> new MemberIqException(ResponseMessages.EXPENSE_NOT_FOUND));
	}

	@Override
	public Page<Expense> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching expenses with criteria: {}", searchCriteria);
		Organization organization = UtilityService.getUserOrganization();

		Specification<Expense> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);

		return expenseRepository.findAll(combinedSpec, pageable);
	}

	private Specification<Expense> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {
		List<Specification<Expense>> specs = searchCriteria.stream().map(ExpenseSpecification::new)
				.collect(Collectors.toList());

		specs.add(new ExpenseSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", false)));
		specs.add(new ExpenseSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public void bulkUpdate(StatusDto statusDto) {
		Organization organization = UtilityService.getUserOrganization();

		List<Expense> expenses = expenseRepository.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(),
				organization.getId());

		if (expenses.isEmpty()) {
			throw new MemberIqException(ResponseMessages.EXPENSE_DATA_NOT_FOUND);
		}

		if (statusDto.getStatus().equals(Status.DELETED)) {
			expenses.forEach(expense -> expense.setIsDeleted(true));
		} else {
			expenses.forEach(expense -> expense.setStatus(statusDto.getStatus()));
		}

		expenseRepository.saveAll(expenses);
	}
}