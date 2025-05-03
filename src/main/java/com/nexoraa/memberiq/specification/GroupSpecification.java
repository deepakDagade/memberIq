package com.nexoraa.memberiq.specification;

import org.springframework.data.jpa.domain.Specification;

import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GroupSpecification implements Specification<Group> {

	private static final long serialVersionUID = 1L;

	private final FilterCriteria criteria;

	public GroupSpecification(FilterCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// Use constants instead of hardcoded strings
		switch (criteria.getKey()) {
		case GlobalConstants.NAME:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.NAME)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");

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