package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.ExpenseCategory;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.ExpenseCategoryRepository;
import com.nexoraa.memberiq.specification.ExpenseCategorySpecification;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

	private final ExpenseCategoryRepository expenseCategoryRepository;

	public ExpenseCategoryServiceImpl(ExpenseCategoryRepository expenseCategoryRepository) {
		this.expenseCategoryRepository = expenseCategoryRepository;
	}

	@Override
	public ExpenseCategory save(ExpenseCategory expenseCategory) {
		Organization organization = UtilityService.getUserOrganization();
		expenseCategory.setOrganization(organization);

		// Validate for duplicate category name within the organization
		validateExpenseCategoryName(expenseCategory.getName(), organization.getId());
		expenseCategory.setIsDeleted(false);

		return expenseCategoryRepository.save(expenseCategory);
	}

	private void validateExpenseCategoryName(String categoryName, UUID organizationId) {
		Optional<ExpenseCategory> dbCategory = expenseCategoryRepository
				.findByNameAndOrganizationIdAndIsDeletedFalse(categoryName, organizationId);
		if (dbCategory.isPresent()) {
			throw new MemberIqException(
					String.format(ResponseMessages.EXPENSE_CATEGORY_NAME_ALREADY_EXISTS, categoryName));
		}
	}

	@Override
	public ExpenseCategory update(ExpenseCategory expenseCategory) {
		Organization organization = UtilityService.getUserOrganization();
		expenseCategory.setOrganization(organization);

		// Validate if category exists
		ExpenseCategory dbCategory = validateExpenseCategoryName(expenseCategory.getId(), expenseCategory.getName(),
				organization.getId());

		dbCategory.setName(expenseCategory.getName());
		dbCategory.setDescription(expenseCategory.getDescription());

		return expenseCategoryRepository.save(dbCategory);
	}

	private ExpenseCategory validateExpenseCategoryName(UUID id, String categoryName, UUID organizationId) {
		List<ExpenseCategory> categories = expenseCategoryRepository
				.findByIdOrNameAndOrganizationIdAndIsDeletedFalse(id, categoryName, organizationId);

		if (categories.isEmpty()) {
			throw new MemberIqException(ResponseMessages.EXPENSE_CATEGORY_NOT_FOUND);
		}
		if (categories.size() > GlobalConstants.ONE) {
			for (ExpenseCategory category : categories) {
				if (!id.equals(category.getId())) {
					if (categoryName.equalsIgnoreCase(category.getName())) {
						throw new MemberIqException(
								String.format(ResponseMessages.EXPENSE_CATEGORY_NAME_ALREADY_EXISTS, categoryName));
					}
				}
			}
		}
		return categories.get(GlobalConstants.ZERO);
	}

	@Override
	public Page<ExpenseCategory> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching expense categories with criteria: {}", searchCriteria);
		Organization organization = UtilityService.getUserOrganization();
		Specification<ExpenseCategory> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);
		return expenseCategoryRepository.findAll(combinedSpec, pageable);
	}

	private Specification<ExpenseCategory> createSpecifications(List<FilterCriteria> searchCriteria,
			UUID organizationId) {
		List<Specification<ExpenseCategory>> specs = searchCriteria.stream().map(ExpenseCategorySpecification::new)
				.collect(Collectors.toList());

		specs.add(new ExpenseCategorySpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new ExpenseCategorySpecification(
				new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public List<ExpenseCategory> findByOrganization() {
		Organization organization = UtilityService.getUserOrganization();
		return expenseCategoryRepository.findByOrganizationIdAndStatusAndIsDeletedFalse(organization.getId(),
				Status.ACTIVE);
	}

	@Override
	public void bulkUpdate(StatusDto statusDto) {
		Organization organization = UtilityService.getUserOrganization();
		List<ExpenseCategory> categories = expenseCategoryRepository
				.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(), organization.getId());

		if (categories.isEmpty()) {
			throw new MemberIqException(ResponseMessages.EXPENSE_CATEGORIES_DATA_NOT_FOUND);
		}
		if (statusDto.getStatus().equals(Status.DELETED)) {
			categories.forEach(categorie -> categorie.setIsDeleted(true));
		} else {
			categories.forEach(categorie -> categorie.setStatus(statusDto.getStatus()));
		}

		categories.forEach(category -> {

			category.setStatus(statusDto.getStatus());
			category.setIsDeleted(statusDto.getStatus().equals(Status.DELETED));
		});

		expenseCategoryRepository.saveAll(categories);
	}
}