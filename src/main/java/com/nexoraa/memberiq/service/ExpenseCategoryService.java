package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.ExpenseCategory;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface ExpenseCategoryService {

    ExpenseCategory save(ExpenseCategory expenseCategory);

    ExpenseCategory update(ExpenseCategory expenseCategory);

    void bulkUpdate(StatusDto statusDto);

    Page<ExpenseCategory> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

    List<ExpenseCategory> findByOrganization();
}