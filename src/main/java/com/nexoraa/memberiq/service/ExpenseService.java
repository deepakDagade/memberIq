package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Expense;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface ExpenseService {

	Expense save(Expense expense);

	Expense update(Expense expense);

	void bulkUpdate(StatusDto statusDto);

	Page<Expense> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

}