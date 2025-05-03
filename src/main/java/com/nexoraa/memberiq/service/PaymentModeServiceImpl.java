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
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.entity.PaymentMode;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.PaymentModeRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.PaymentModeSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentModeServiceImpl implements PaymentModeService {

	private final PaymentModeRepository paymentModeRepository;

	public PaymentModeServiceImpl(PaymentModeRepository paymentModeRepository) {
		this.paymentModeRepository = paymentModeRepository;
	}

	@Override
	public PaymentMode save(PaymentMode paymentMode) {
		Organization organization = UtilityService.getUserOrganization();
		validatePaymentModeName(paymentMode.getName(), organization.getId());
		paymentMode.setOrganization(organization);
		paymentMode.setIsDeleted(false);
		return paymentModeRepository.save(paymentMode);
	}

	private void validatePaymentModeName(String name, UUID organizationId) {
		Optional<PaymentMode> existingPaymentMode = paymentModeRepository
				.findByNameAndOrganizationIdAndIsDeletedFalse(name, organizationId);
		if (existingPaymentMode.isPresent()) {
			throw new MemberIqException(String.format(ResponseMessages.PAYMENT_MODE_NAME_ALREADY_EXISTS, name));
		}
	}

	@Override
	public Page<PaymentMode> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching PaymentModes with criteria: {}", searchCriteria);

		Organization organization = UtilityService.getUserOrganization();

		Specification<PaymentMode> combinedSpec = createSpecifications(searchCriteria, organization.getId());
		return paymentModeRepository.findAll(combinedSpec, pageable);
	}

	private Specification<PaymentMode> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {

		List<Specification<PaymentMode>> specs = searchCriteria.stream().map(PaymentModeSpecification::new)
				.collect(Collectors.toList());

		specs.add(new PaymentModeSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new PaymentModeSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public PaymentMode update(PaymentMode paymentMode) {

		Organization organization = UtilityService.getUserOrganization();

		PaymentMode dbPaymentMode = validatePaymentModeName(paymentMode.getId(), paymentMode.getName(),
				organization.getId());

		dbPaymentMode = copyFields(dbPaymentMode, paymentMode);

		return paymentModeRepository.save(dbPaymentMode);

	}

	private PaymentMode copyFields(PaymentMode dbPaymentMode, PaymentMode paymentMode) {
		dbPaymentMode.setLogoUrl(paymentMode.getLogoUrl());
		dbPaymentMode.setName(paymentMode.getName());
		dbPaymentMode.setStatus(paymentMode.getStatus());

		return dbPaymentMode;
	}

	private PaymentMode validatePaymentModeName(UUID id, String name, UUID organizationId) {

		List<PaymentMode> existingPaymentModes = paymentModeRepository
				.findByIdOrNameAndOrganizationIdAndIsDeletedFalse(id, name, organizationId);

		if (existingPaymentModes.isEmpty()) {
			throw new MemberIqException(ResponseMessages.PAYMENT_MODES_DATA_NOT_FOUND);
		}
		if (existingPaymentModes.size() != GlobalConstants.ONE) {
			throw new MemberIqException(String.format(ResponseMessages.PAYMENT_MODE_NAME_ALREADY_EXISTS, name));
		}
		return existingPaymentModes.get(GlobalConstants.ZERO);
	}

	@Override
	public List<PaymentMode> findAll() {

		Organization organization = UtilityService.getUserOrganization();
		paymentModeRepository.findByOrganizationIdAndStatusAndIsDeletedFalse(organization.getId(), Status.ACTIVE);
		return paymentModeRepository.findAll().stream().filter(paymentMode -> !paymentMode.getIsDeleted())
				.collect(Collectors.toList());
	}

	@Override
	public void bulkUpdateStatus(StatusDto statusDto) {
		
		Organization organization = UtilityService.getUserOrganization();

		List<PaymentMode> paymentModes = paymentModeRepository
				.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(), organization.getId());
		
		if (!paymentModes.isEmpty()) {
			if (statusDto.getStatus().equals(Status.DELETED)) {
				paymentModes.forEach(group -> group.setIsDeleted(true));
			} else {
				paymentModes.forEach(group -> group.setStatus(statusDto.getStatus()));
			}
			paymentModeRepository.saveAll(paymentModes);
		}

	}
}