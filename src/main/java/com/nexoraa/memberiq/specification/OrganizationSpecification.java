package com.nexoraa.memberiq.specification;

import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.nexoraa.memberiq.entity.Address;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrganizationSpecification implements Specification<Organization> {
	private static final long serialVersionUID = 1L;
// make static variable 
	private FilterCriteria criteria;

	@Override
	public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

		if (Objects.nonNull(criteria.getKey())) {
			Join<Organization, Address> addressJoin = null;
			switch (criteria.getKey()) {

			case GlobalConstants.NAME:
				return criteriaBuilder.like(criteriaBuilder.lower(root.get(GlobalConstants.NAME)),
						"%" + criteria.getValue().toString().toLowerCase() + "%");
			case GlobalConstants.ADDRESS_NAME:
				addressJoin = root.join(GlobalConstants.ADDRESS);
				return criteriaBuilder.equal(addressJoin.get(GlobalConstants.ADDRESS_NAME), criteria.getValue());
			case GlobalConstants.COUNTRY:
				addressJoin = root.join(GlobalConstants.ADDRESS);
				return criteriaBuilder.equal(addressJoin.get(GlobalConstants.COUNTRY), criteria.getValue());
			case GlobalConstants.STATE:
				addressJoin = root.join(GlobalConstants.ADDRESS);
				return criteriaBuilder.equal(addressJoin.get(GlobalConstants.STATE), criteria.getValue());
			case GlobalConstants.STATUS:
				try {
					Status statusEnum = Status.valueOf(criteria.getValue().toString().toUpperCase());
					return criteriaBuilder.equal(root.get(GlobalConstants.STATUS), statusEnum);
				} catch (IllegalArgumentException e) {
					throw new MemberIqException(
							String.format(ValidationMessages.INVALID_STATUS_VALUE, criteria.getValue()));
				}

			}
		}
		return null;
	}

}
