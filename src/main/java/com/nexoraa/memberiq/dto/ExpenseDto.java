package com.nexoraa.memberiq.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.nexoraa.memberiq.entity.Expense;
import com.nexoraa.memberiq.enums.Status;

import lombok.Data;

@Data
public class ExpenseDto {
	private UUID id;

	private BigDecimal amount;

	private LocalDate expenseDate;

	private String description;

	private String receiptUrl;

	private boolean isRecurring;

	private ProfileDto createdByProfile;

	private OrganizationDto organization;

	private ExpenseCategoryDto category;

	private Status status;

	public Expense toExpense() {
		return Expense.builder().id(this.id).amount(amount).expenseDate(expenseDate).description(description)
				.receiptUrl(receiptUrl).isRecurring(isRecurring)
				.createdByProfile(Objects.nonNull(createdByProfile) ? createdByProfile.toProfile() : null)
				.organization(Objects.nonNull(organization) ? organization.toOrganization() : null)
				.category(Objects.nonNull(category) ? category.toExpenseCategory() : null).status(this.status).build();
	}
}
