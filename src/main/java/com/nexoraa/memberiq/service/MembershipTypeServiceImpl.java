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
import com.nexoraa.memberiq.entity.MembershipType;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.MembershipTypeRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.MembershipTypeSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MembershipTypeServiceImpl implements MembershipTypeService {

	private final MembershipTypeRepository membershipTypeRepository;

	public MembershipTypeServiceImpl(MembershipTypeRepository membershipTypeRepository) {
		this.membershipTypeRepository = membershipTypeRepository;
	}

	@Override
	public MembershipType save(MembershipType membershipType) {
		Organization userOrganization = UtilityService.getUserOrganization();
		membershipType.setOrganization(userOrganization);

		validateMembershipTypeName(membershipType.getName(), membershipType.getOrganization().getId());

		membershipType.setIsDeleted(false);
		return membershipTypeRepository.save(membershipType);
	}

	private void validateMembershipTypeName(String name, UUID organizationId) {
		Optional<MembershipType> existingMembershipType = membershipTypeRepository
				.findByNameAndOrganizationIdAndIsDeletedFalse(name, organizationId);
		if (existingMembershipType.isPresent()) {
			throw new MemberIqException(String.format(ResponseMessages.MEMBERSHIP_TYPE_NAME_ALREADY_EXISTS, name));
		}
	}

	@Override
	public Page<MembershipType> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching PaymentModes with criteria: {}", searchCriteria);

		Organization organization = UtilityService.getUserOrganization();

		Specification<MembershipType> combinedSpec = createSpecifications(searchCriteria, organization.getId());
		return membershipTypeRepository.findAll(combinedSpec, pageable);
	}

	private Specification<MembershipType> createSpecifications(List<FilterCriteria> searchCriteria,
			UUID organizationId) {
		List<Specification<MembershipType>> specs = searchCriteria.stream().map(MembershipTypeSpecification::new)
				.collect(Collectors.toList());

		specs.add(new MembershipTypeSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(
				new MembershipTypeSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public MembershipType update(MembershipType membershipType) {

		Organization userOrganization = UtilityService.getUserOrganization();
		membershipType.setOrganization(userOrganization);
		MembershipType dbMembershipType = validateMembershipType(membershipType);

		dbMembershipType = copyFields(dbMembershipType, membershipType);
		return membershipTypeRepository.save(membershipType);
	}

	private MembershipType copyFields(MembershipType dbMembershipType, MembershipType membershipType) {
		dbMembershipType.setName(membershipType.getName());
		dbMembershipType.setMinCost(membershipType.getMinCost());
		dbMembershipType.setMaxCost(membershipType.getMaxCost());
		dbMembershipType.setDuration(membershipType.getDuration());
		dbMembershipType.setStatus(membershipType.getStatus());
		return dbMembershipType;
	}

	private MembershipType validateMembershipType(MembershipType membershipType) {
		List<MembershipType> dbMembershipType = membershipTypeRepository
				.findByIdOrNameAndOrganizationIdAndIsDeletedFalse(membershipType.getId(), membershipType.getName(),
						membershipType.getOrganization().getId());
		if (dbMembershipType.isEmpty()) {
			throw new MemberIqException(ResponseMessages.MEMBERSHIP_TYPE_NOT_FOUND);
		}
		if (dbMembershipType.size() != GlobalConstants.ONE) {
			throw new MemberIqException(
					String.format(ResponseMessages.MEMBERSHIP_TYPE_NAME_ALREADY_EXISTS, membershipType.getName()));
		}
		return dbMembershipType.get(GlobalConstants.ZERO);
	}

	@Override
	public void BulkUpdateStatus(StatusDto statusDto) {
		Organization userOrganization = UtilityService.getUserOrganization();

		List<MembershipType> membershipTypes = membershipTypeRepository
				.findByIdInAndAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(), userOrganization.getId());

		if (!membershipTypes.isEmpty()) {
			if (statusDto.getStatus().equals(Status.DELETED)) {
				membershipTypes.forEach(group -> group.setIsDeleted(true));
			} else {
				membershipTypes.forEach(group -> group.setStatus(statusDto.getStatus()));
			}
			membershipTypeRepository.saveAll(membershipTypes);
		}

	}

	@Override
	public List<MembershipType> findAll() {
		Organization userOrganization = UtilityService.getUserOrganization();
		return membershipTypeRepository.findByOrganizationIdAndStatusAndIsDeletedFalse(userOrganization.getId(),
				Status.ACTIVE);

	}

}