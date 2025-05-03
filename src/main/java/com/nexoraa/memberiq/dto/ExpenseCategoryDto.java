package com.nexoraa.memberiq.dto;

import java.util.UUID;

import com.nexoraa.memberiq.entity.ExpenseCategory;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseCategoryDto {
	private UUID id;

	@NotBlank(message = ValidationMessages.EXPENCE_CATEGORY_NAME_REQUIRED)
    private String name;

    private String description;

    @NotNull(message = ValidationMessages.STATUS_REQUIRED)
	private Status status;

    public ExpenseCategory toExpenseCategory() {
        return ExpenseCategory.builder()
                .id(id)
                .name(name)
                .description(description).status(status)
                .build();
    }
}
