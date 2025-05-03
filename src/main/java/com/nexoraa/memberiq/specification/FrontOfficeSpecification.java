package com.nexoraa.memberiq.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import com.nexoraa.memberiq.entity.FrontOffice;
import com.nexoraa.memberiq.enums.EnquiryType;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.utility.DateUtil;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class FrontOfficeSpecification implements Specification<FrontOffice> {

	private static final long serialVersionUID = 1L;

	private final FilterCriteria criteria;

	public FrontOfficeSpecification(FilterCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<FrontOffice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		switch (criteria.getKey()) {
		case GlobalConstants.NAME:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.NAME)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");

		case GlobalConstants.EMAIL:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.EMAIL)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");

		case GlobalConstants.PHONE:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.PHONE)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");

		case GlobalConstants.ENQUIRYTYPE:
			try {
				EnquiryType statusEnum = EnquiryType.valueOf(criteria.getValue().toString().toUpperCase());
				return criteriaBuilder.equal(root.get(GlobalConstants.ENQUIRYTYPE), statusEnum);
			} catch (IllegalArgumentException e) {
				throw new MemberIqException(ResponseMessages.INVALID_STATUS_VALUE + criteria.getValue());
			}

		case GlobalConstants.ENQUIRY_DATE:
			Pair<LocalDate, LocalDate> dateRange = DateUtil.parseDateRange(criteria.getValue().toString());
			return criteriaBuilder.and(
					criteriaBuilder.greaterThanOrEqualTo(root.get(GlobalConstants.ENQUIRY_DATE), dateRange.getFirst()),
					criteriaBuilder.lessThanOrEqualTo(root.get(GlobalConstants.ENQUIRY_DATE), dateRange.getSecond()));

		case GlobalConstants.FOLLOW_UP_DATE:
			Pair<LocalDate, LocalDate> FollowupDateRange = DateUtil.parseDateRange(criteria.getValue().toString());
			return criteriaBuilder.and(
					criteriaBuilder.greaterThanOrEqualTo(root.get(GlobalConstants.FOLLOW_UP_DATE),
							FollowupDateRange.getFirst()),
					criteriaBuilder.lessThanOrEqualTo(root.get(GlobalConstants.FOLLOW_UP_DATE),
							FollowupDateRange.getSecond()));

		case GlobalConstants.NEXT_FOLLOWUP_DATE:
			Pair<LocalDate, LocalDate> nextFollowupDateRange = DateUtil.parseDateRange(criteria.getValue().toString());
			return criteriaBuilder.and(
					criteriaBuilder.greaterThanOrEqualTo(root.get(GlobalConstants.NEXT_FOLLOWUP_DATE),
							nextFollowupDateRange.getFirst()),
					criteriaBuilder.lessThanOrEqualTo(root.get(GlobalConstants.NEXT_FOLLOWUP_DATE),
							nextFollowupDateRange.getSecond()));

		case GlobalConstants.IS_DELETED:
			return criteriaBuilder.equal(root.get(GlobalConstants.IS_DELETED), criteria.getValue());

		case GlobalConstants.ORGANIZATION:
			return criteriaBuilder.equal(root.get(GlobalConstants.ORGANIZATION).get("id"), criteria.getValue());

		default:
			return null; // When there's no match
		}
	}

}
