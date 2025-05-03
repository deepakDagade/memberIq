package com.nexoraa.memberiq.specification;

import org.springframework.data.jpa.domain.Specification;

import com.nexoraa.memberiq.entity.Profile;
import com.nexoraa.memberiq.enums.ProfileType;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProfileSpecification implements Specification<Profile> {
	private static final long serialVersionUID = 1L;
	private final FilterCriteria criteria;

	public ProfileSpecification(FilterCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// Use constants instead of hardcoded strings
		switch (criteria.getKey()) {
		case GlobalConstants.FIRST_NAME:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.FIRST_NAME)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");
		case GlobalConstants.LAST_NAME:
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.LAST_NAME)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");
		case GlobalConstants.STATUS:
			try {
				Status statusEnum = Status.valueOf(criteria.getValue().toString().toUpperCase());
				return criteriaBuilder.equal(root.get(GlobalConstants.STATUS), statusEnum);
			} catch (IllegalArgumentException e) {
				throw new MemberIqException(ResponseMessages.INVALID_STATUS_VALUE + criteria.getValue());
			}
		case GlobalConstants.GENDER:
			return criteriaBuilder.equal(root.get(GlobalConstants.GENDER), criteria.getValue());
		case GlobalConstants.DATE_OF_BIRTH:
			return criteriaBuilder.equal(root.get(GlobalConstants.DATE_OF_BIRTH), criteria.getValue());

		case GlobalConstants.PROFILE_TYPE:
			try {
				ProfileType profileTypeEnum = ProfileType.valueOf(criteria.getValue().toString().toUpperCase());
				return criteriaBuilder.equal(root.get(GlobalConstants.PROFILE_TYPE), profileTypeEnum);
			} catch (IllegalArgumentException e) {
				throw new MemberIqException(ResponseMessages.INVALID_PROFILE_TYPE_VALUE + criteria.getValue());
			}
		case GlobalConstants.ORGANIZATION:
			return criteriaBuilder.equal(root.get(GlobalConstants.ORGANIZATION).get("id"), criteria.getValue());
		case GlobalConstants.IS_DELETED:
			return criteriaBuilder.equal(root.get(GlobalConstants.IS_DELETED), criteria.getValue());
		default:
			return null; // When there's no match
		}
	}
}