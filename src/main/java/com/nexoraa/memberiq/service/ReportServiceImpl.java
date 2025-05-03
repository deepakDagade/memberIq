package com.nexoraa.memberiq.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.projection.DateWiseCollectionProjection;
import com.nexoraa.memberiq.projection.GenderWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MembershipWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MonthWiseCollectionProjection;
import com.nexoraa.memberiq.projection.PaymentTypeWiseCollectionProjection;
import com.nexoraa.memberiq.projection.WeekWiseCollectionProjection;
import com.nexoraa.memberiq.repository.PaymentDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

	private PaymentDetailsRepository paymentDetailsRepository;

	public ReportServiceImpl(PaymentDetailsRepository paymentDetailsRepository) {

		this.paymentDetailsRepository = paymentDetailsRepository;
	}

	@Override
	public List<PaymentTypeWiseCollectionProjection> FindyCollectionByPaymentType(UUID organizationId,
			LocalDate startDate, LocalDate endDate) {
		return paymentDetailsRepository.getCollectionByPaymentType(organizationId, startDate, endDate);
	}

	@Override
	public List<GenderWiseCollectionProjection> getGenderWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate) {
		return paymentDetailsRepository.getGenderWiseCollection(organizationId, startDate, endDate);
	}

	@Override
	public List<MembershipWiseCollectionProjection> getCollectionByMembershipType(UUID organizationId,
			LocalDate startDate, LocalDate endDate) {
		return paymentDetailsRepository.getCollectionByMembershipTypeBetweenDates(organizationId, startDate, endDate);

	}

	@Override
	public List<DateWiseCollectionProjection> getDateWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate) {
		return paymentDetailsRepository.findDateWiseCollection(startDate, endDate, organizationId);

	}

	@Override
	public List<WeekWiseCollectionProjection> getWeekWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate) {
		return paymentDetailsRepository.findWeekWiseCollection(startDate, endDate, organizationId);
	}

	@Override
	public List<MonthWiseCollectionProjection> getMonthWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate) {
		return paymentDetailsRepository.findMonthWiseCollection(startDate, endDate, organizationId);

	}

}
