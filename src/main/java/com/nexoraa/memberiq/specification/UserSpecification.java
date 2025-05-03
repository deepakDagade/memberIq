package com.nexoraa.memberiq.specification;

import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.GlobalConstants;

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
public class UserSpecification implements Specification<AppUser> {

	private static final long serialVersionUID = 1L;

	private FilterCriteria criteria;

	@Override
	public Predicate toPredicate(Root<AppUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (Objects.nonNull(criteria.getKey())) {
			switch (criteria.getKey()) {
			case GlobalConstants.NAME:
				String nameValue = "%" + criteria.getValue().toString().toLowerCase() + "%";

				Predicate firstNamePredicate = criteriaBuilder
						.like(criteriaBuilder.lower(root.get(GlobalConstants.FIRST_NAME)), nameValue);

				Predicate middleNamePredicate = criteriaBuilder
						.like(criteriaBuilder.lower(root.get(GlobalConstants.MIDDLE_NAME)), nameValue);

				Predicate lastNamePredicate = criteriaBuilder
						.like(criteriaBuilder.lower(root.get(GlobalConstants.LAST_NAME)), nameValue);

				return criteriaBuilder.or(firstNamePredicate, middleNamePredicate, lastNamePredicate);

			case GlobalConstants.STATUS:
				try {
					Status statusEnum = Status.valueOf(criteria.getValue().toString().toUpperCase());
					return criteriaBuilder.equal(root.get(GlobalConstants.STATUS), statusEnum);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Invalid status value: " + criteria.getValue());
				}

			case GlobalConstants.ORGANIZATION:
				Join<AppUser, Organization> organizationJoin = root.join(GlobalConstants.ORGANIZATION);
				return criteriaBuilder.equal(organizationJoin.get(GlobalConstants.ID), criteria.getValue());

			case GlobalConstants.GROUP:
				Join<AppUser, Group> groupJoin = root.join(GlobalConstants.GROUP);
				return criteriaBuilder.equal(groupJoin.get(GlobalConstants.ID), criteria.getValue());

			default:
				return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		return null;
	}

}
