package com.nexoraa.memberiq.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import com.nexoraa.memberiq.entity.Expense;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.utility.DateUtil;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ExpenseSpecification implements Specification<Expense> {

	private static final long serialVersionUID = 1L;

	private final FilterCriteria criteria;

	public ExpenseSpecification(FilterCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		switch (criteria.getKey()) {
		case GlobalConstants.AMOUNT:
			return criteriaBuilder.equal(root.get(GlobalConstants.AMOUNT), criteria.getValue());

		case GlobalConstants.EXPENSE_DATE:
			Pair<LocalDate, LocalDate> dateRange = DateUtil.parseDateRange(criteria.getValue().toString());
			return criteriaBuilder.and(
					criteriaBuilder.greaterThanOrEqualTo(root.get(GlobalConstants.EXPENSE_DATE), dateRange.getFirst()),
					criteriaBuilder.lessThanOrEqualTo(root.get(GlobalConstants.EXPENSE_DATE), dateRange.getSecond()));

		case GlobalConstants.STATUS:
			try {
				Status statusEnum = Status.valueOf(criteria.getValue().toString().toUpperCase());
				return criteriaBuilder.equal(root.get(GlobalConstants.STATUS), statusEnum);
			} catch (IllegalArgumentException e) {
				throw new MemberIqException(ResponseMessages.INVALID_STATUS_VALUE + criteria.getValue());
			}

		case GlobalConstants.IS_DELETED:
			return criteriaBuilder.equal(root.get(GlobalConstants.IS_DELETED), criteria.getValue());

		case GlobalConstants.ORGANIZATION:
			return criteriaBuilder.equal(root.get(GlobalConstants.ORGANIZATION).get("id"), criteria.getValue());

		default:
			return null; // When there's no match
		}
	}
}