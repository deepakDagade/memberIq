package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.OrganizationRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.OrganizationSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	private static final int SINGLE_ORGANIZATION = 1;
	private final OrganizationRepository organizationRepository;

	public OrganizationServiceImpl(OrganizationRepository organizationRepository) {

		this.organizationRepository = organizationRepository;
	}

	@Override
	public Organization save(Organization organization) {
		Organization dbOrganization = organizationRepository.findByNameAndIsDeletedFalse(organization.getName());
		if (Objects.nonNull(dbOrganization)) {
			throw new MemberIqException(ResponseMessages.ORGANIZATION_ALREADY_EXIST);
		}
		organization.setIsDeleted(Boolean.FALSE);
		return organizationRepository.save(organization);
	}

	@Override
	public Organization update(Organization organization) {
		// Fetch organizations by ID or name where isDeleted is false.
		List<Organization> dbOrganizations = organizationRepository
				.findByIdOrNameAndIsDeletedFalse(organization.getId(), organization.getName());
		if (Objects.nonNull(dbOrganizations)) {
			// If the list contains exactly one organization, proceed to update the
			// organization.
			if (dbOrganizations.size() == SINGLE_ORGANIZATION) {
				Organization dbOrganization = copyFields(organization, dbOrganizations.get(GlobalConstants.ZERO));

				return organizationRepository.save(dbOrganization);
			} else {
				throw new MemberIqException(ResponseMessages.ORGANIZATION_ALREADY_EXIST);
			}
		} else {
			throw new MemberIqException(ResponseMessages.ORGANIZATION_NOT_FOUND);
		}
	}

	private Organization copyFields(Organization organization, Organization dbOrganization) {
		dbOrganization.setAddress(organization.getAddress());
		dbOrganization.setStatus(organization.getStatus());
		dbOrganization.setContactNumber(organization.getContactNumber());
		dbOrganization.setName(organization.getName());
		dbOrganization.setLogoUrl(organization.getLogoUrl());
		dbOrganization.setType(organization.getType());

		return dbOrganization;
	}

	@Override
	public Page<Organization> BySearchCriteria(List<FilterCriteria> filterCriteria, Pageable pageable) {

		Page<Organization> organizations = findAllByIsDeletedFalse(filterCriteria, pageable);
		if (organizations.isEmpty()) {
			throw new MemberIqException(ResponseMessages.ORGANIZATION_NOT_FOUND);
		}
		return organizations;
	}

	private Page<Organization> findAllByIsDeletedFalse(List<FilterCriteria> filterCriteria, Pageable pageable) {

		List<Specification<Organization>> specs = filterCriteria.stream().map(criteria -> new OrganizationSpecification(
				new FilterCriteria(criteria.getKey(), ":", criteria.getValue()))).collect(Collectors.toList());

		specs.add(new OrganizationSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", false)));

		// Combine all specifications using an AND operator
		Specification<Organization> combinedSpec = specs.stream().reduce(Specification::and).orElse(null);
		pageable = UtilityService.applySorting(pageable);
		return organizationRepository.findAll(combinedSpec, pageable);

	}

	@Override
	public Organization findOrganizationById(UUID id) {
		return organizationRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new MemberIqException(ResponseMessages.ORGANIZATION_NOT_FOUND));

	}

	@Override
	public String uploadAccountLogo(MultipartFile logo) {
		// TODO Auto-generated method stub
		return null;
	}

}
