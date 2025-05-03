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
import com.nexoraa.memberiq.entity.PaymentDetails;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.PaymentDetailsRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.PaymentDetailsSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

	private final PaymentDetailsRepository paymentDetailsRepository;

	public PaymentDetailsServiceImpl(PaymentDetailsRepository paymentDetailsRepository) {
		this.paymentDetailsRepository = paymentDetailsRepository;
	}

	@Override
	public PaymentDetails save(PaymentDetails paymentDetails) {
		Organization organization = UtilityService.getUserOrganization();

		// Set organization and default fields as necessary
		paymentDetails.setIsDeleted(false);
		paymentDetails.setOrganization(organization);

		return paymentDetailsRepository.save(paymentDetails);
	}

	@Override
	public PaymentDetails update(PaymentDetails paymentDetails) {

		Organization organization = UtilityService.getUserOrganization();

		// Validate and update fields
		Optional<PaymentDetails> existingPaymentDetails = paymentDetailsRepository
				.findByIdAndOrganizationId(paymentDetails.getId(), organization.getId());
		if (!existingPaymentDetails.isPresent()) {
			throw new MemberIqException(ResponseMessages.PAYMENT_DETAIL_NOT_FOUND);
		}

		PaymentDetails dbPaymentDetails = existingPaymentDetails.get();

		dbPaymentDetails = copyFields(dbPaymentDetails, paymentDetails);

		return paymentDetailsRepository.save(dbPaymentDetails);
	}

	private PaymentDetails copyFields(PaymentDetails dbPaymentDetails, PaymentDetails paymentDetails) {
		dbPaymentDetails.setPaymentAmount(paymentDetails.getPaymentAmount());
		dbPaymentDetails.setDate(paymentDetails.getDate());
		dbPaymentDetails.setComment(paymentDetails.getComment());
		dbPaymentDetails.setPaymentMode(paymentDetails.getPaymentMode());
		dbPaymentDetails.setCollectBy(paymentDetails.getCollectBy());
		dbPaymentDetails.setMember(paymentDetails.getMember());
		return dbPaymentDetails;
	}

	@Override
	public void bulkUpdate(StatusDto statusDto) {

		Organization organization = UtilityService.getUserOrganization();
		// Logic for bulk updating payment details
		List<PaymentDetails> paymentDetailsList = paymentDetailsRepository
				.findByIdInAndOrganizationId(statusDto.getIds(), organization.getId());
		if (paymentDetailsList.isEmpty()) {
			throw new MemberIqException(ResponseMessages.PAYMENT_DETAILS_DATA_NOT_FOUND);
		}

		paymentDetailsList.forEach(group -> group.setIsDeleted(true));

		paymentDetailsRepository.saveAll(paymentDetailsList);
	}

	@Override
	public Page<PaymentDetails> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		Organization organization = UtilityService.getUserOrganization();
		Specification<PaymentDetails> combinedSpec = createSpecifications(searchCriteria, organization.getId());
		return paymentDetailsRepository.findAll(combinedSpec, pageable);
	}

	private Specification<PaymentDetails> createSpecifications(List<FilterCriteria> searchCriteria,
			UUID organizationId) {
		List<Specification<PaymentDetails>> specs = searchCriteria.stream().map(PaymentDetailsSpecification::new)
				.collect(Collectors.toList());

		specs.add(new PaymentDetailsSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(
				new PaymentDetailsSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);

	}

}