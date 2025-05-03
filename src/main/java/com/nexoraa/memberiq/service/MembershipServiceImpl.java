package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Membership;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.MembershipRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.MembershipSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MembershipServiceImpl implements MembershipService {

	private final MembershipRepository membershipRepository;

	public MembershipServiceImpl(MembershipRepository membershipRepository) {
		this.membershipRepository = membershipRepository;
	}

	@Override
	public Membership save(Membership membership) {
		Organization organization = UtilityService.getUserOrganization();
		membership.setOrganization(organization);
		membership.setIsDeleted(Boolean.FALSE);

		validateMembership(membership);

		return membershipRepository.save(membership);
	}

	private void validateMembership(Membership membership) {
		Optional<Membership> existingMembership = membershipRepository
				.findByIdAndOrganizationIdAndIsDeletedFalse(membership.getId(), membership.getOrganization().getId());
		if (existingMembership.isPresent()) {
			throw new MemberIqException(ResponseMessages.MEMBERSHIP_ALREADY_EXISTS);
		}
	}

	@Override
	public Membership update(Membership membership) {

		Organization organization = UtilityService.getUserOrganization();

		membership.setOrganization(organization);
		Membership existingMembership = validateMembershipExists(membership.getId(), organization.getId());
		existingMembership = copyFields(existingMembership, membership);

		return membershipRepository.save(existingMembership);
	}

	private Membership copyFields(Membership existingMembership, Membership membership) {
		existingMembership.setDiscount(membership.getDiscount());
		existingMembership.setTotalAmount(membership.getTotalAmount());
		existingMembership.setPaidAmount(membership.getPaidAmount());
		existingMembership.setDueAmount(membership.getDueAmount());
		existingMembership.setStartDate(membership.getStartDate());
		existingMembership.setEndDate(membership.getEndDate());
		existingMembership.setAutoRenew(membership.getAutoRenew());
		existingMembership.setStatus(membership.getStatus());

		return existingMembership;
	}

	private Membership validateMembershipExists(UUID id, UUID organizationId) {
		return membershipRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, organizationId)
				.orElseThrow(() -> new MemberIqException(ResponseMessages.MEMBERSHIP_NOT_FOUND));
	}

	@Override
	public Page<Membership> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching memberships with criteria: {}", searchCriteria);
		Organization organization = UtilityService.getUserOrganization();

		Specification<Membership> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);

		return membershipRepository.findAll(combinedSpec, pageable);
	}

	private Specification<Membership> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {
		List<Specification<Membership>> specs = searchCriteria.stream().map(MembershipSpecification::new)
				.collect(Collectors.toList());

		specs.add(new MembershipSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));
		specs.add(new MembershipSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public List<Membership> findByOrganization() {
		Organization organization = UtilityService.getUserOrganization();
		return membershipRepository.findByOrganizationIdAndIsDeletedFalse(organization.getId());
	}

	@Override
	public void bulkUpdate(StatusDto statusDto) {
		Organization organization = UtilityService.getUserOrganization();

		List<Membership> memberships = membershipRepository
				.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(), organization.getId());

		if (memberships.isEmpty()) {
			throw new MemberIqException(ResponseMessages.MEMBERSHIP_DATA_NOT_FOUND);
		}

		memberships.forEach(membership -> {
			if (statusDto.getStatus().equals(Status.DELETED)) {
				membership.setIsDeleted(true);
			} else {
				membership.setStatus(statusDto.getStatus());
			}
		});

		membershipRepository.saveAll(memberships);
	}
}