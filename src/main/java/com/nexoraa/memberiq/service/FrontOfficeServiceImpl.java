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
import com.nexoraa.memberiq.entity.FrontOffice;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.FrontOfficeRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.FrontOfficeSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FrontOfficeServiceImpl implements FrontOfficeService {

	private final FrontOfficeRepository frontOfficeRepository;

	public FrontOfficeServiceImpl(FrontOfficeRepository frontOfficeRepository) {
		this.frontOfficeRepository = frontOfficeRepository;
	}

	@Override
	public FrontOffice save(FrontOffice frontOffice) {
		Organization organization = UtilityService.getUserOrganization();
		frontOffice.setOrganization(organization);
		frontOffice.setIsDeleted(false);
		return frontOfficeRepository.save(frontOffice);
	}

	@Override
	public FrontOffice update(FrontOffice frontOffice) {

		Organization organization = UtilityService.getUserOrganization();

		Optional<FrontOffice> existingFrontOffice = frontOfficeRepository
				.findByIdAndOrganizationIdAndIsDeletedFalse(frontOffice.getId(), organization.getId());

		if (!existingFrontOffice.isPresent()) {
			throw new MemberIqException(ResponseMessages.FRONT_OFFICE_NOT_FOUND);
		}

		FrontOffice dbFrontOffice = existingFrontOffice.get();
		dbFrontOffice = copyFrontOfficeDetails(frontOffice, dbFrontOffice);

		return frontOfficeRepository.save(dbFrontOffice);
	}

	private FrontOffice copyFrontOfficeDetails(FrontOffice frontOffice, FrontOffice dbFrontOffice) {
		dbFrontOffice.setName(frontOffice.getName());
		dbFrontOffice.setEmail(frontOffice.getEmail());
		dbFrontOffice.setPhoneNo(frontOffice.getPhoneNo());
		dbFrontOffice.setEnquiryType(frontOffice.getEnquiryType());
		dbFrontOffice.setEnquiryDate(frontOffice.getEnquiryDate());
		dbFrontOffice.setFollowUpDate(frontOffice.getFollowUpDate());
		dbFrontOffice.setNextFollowUpDate(frontOffice.getNextFollowUpDate());
		dbFrontOffice.setSource(frontOffice.getSource());
		dbFrontOffice.setAddress(frontOffice.getAddress());
		dbFrontOffice.setComment(frontOffice.getComment());

		return dbFrontOffice;
	}

	@Override
	public void bulkUpdate(StatusDto statusDto) {

		Organization organization = UtilityService.getUserOrganization();

		List<FrontOffice> frontOffices = frontOfficeRepository
				.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(), organization.getId());

		if (frontOffices.isEmpty()) {
			throw new MemberIqException(ResponseMessages.FRONT_OFFICES_DATA_NOT_FOUND);
		}
		frontOffices.forEach(frontOffice -> frontOffice.setIsDeleted(true));
		frontOfficeRepository.saveAll(frontOffices);
	}

	@Override
	public Page<FrontOffice> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {

		Organization organization = UtilityService.getUserOrganization();

		Specification<FrontOffice> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);

		return frontOfficeRepository.findAll(combinedSpec, pageable);
	}

	private Specification<FrontOffice> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {
		List<Specification<FrontOffice>> specs = searchCriteria.stream().map(FrontOfficeSpecification::new)
				.collect(Collectors.toList());

		specs.add(new FrontOfficeSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new FrontOfficeSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public List<FrontOffice> findByOrganization() {

		Organization organization = UtilityService.getUserOrganization();

		return frontOfficeRepository.findByOrganizationIdAndIsDeletedFalse(organization.getId());
	}
}