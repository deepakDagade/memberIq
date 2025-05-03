package com.nexoraa.memberiq.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.DashboardResponseDto;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.projection.DateWiseCollectionProjection;
import com.nexoraa.memberiq.repository.MembershipRepository;
import com.nexoraa.memberiq.repository.PaymentDetailsRepository;
import com.nexoraa.memberiq.utility.UtilityService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private final PaymentDetailsRepository paymentDetailsRepository;
	private final MembershipRepository membershipRepository;

	public DashboardServiceImpl(PaymentDetailsRepository paymentDetailsRepository,
			MembershipRepository membershipRepository) {
		this.paymentDetailsRepository = paymentDetailsRepository;
		this.membershipRepository = membershipRepository;
	}

	@Override
	public DashboardResponseDto getDashboardData() {
		DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();
		LocalDate today = LocalDate.now();
		LocalDate oneWeekAgo = today.minusWeeks(1);
		LocalDate oneMonthBefore = today.minusMonths(1);

		Organization organization = UtilityService.getUserOrganization();
		UUID organizationId = organization.getId();
		long todayPlanExpiry = membershipRepository.countMembershipsByEndDate(organizationId, today);

		long todayRenewal = membershipRepository.countMembershipsByEndDate(organizationId, today);

		List<DateWiseCollectionProjection> dateWiseCollection = paymentDetailsRepository.findDateWiseCollection(today,
				oneMonthBefore, organizationId);

		List<DateWiseCollectionProjection> dateWiseCollectionReversed = dateWiseCollection.reversed();

		dashboardResponseDto = getCollection(dateWiseCollectionReversed, dashboardResponseDto, today, oneWeekAgo);

		return dashboardResponseDto;
	}

	private DashboardResponseDto getCollection(List<DateWiseCollectionProjection> dateWiseCollectionReversed,
			DashboardResponseDto dashboardResponseDto, LocalDate today, LocalDate oneWeekAgo) {
		Double weeklyCollection = 0.0;
		Double monthlyCollection = 0.0;
		for (DateWiseCollectionProjection dateWiseCollectionProjection : dateWiseCollectionReversed) {
			if (dateWiseCollectionProjection.getDate().equals(today)) {
				dashboardResponseDto
						.setTodaysCollection(BigDecimal.valueOf(dateWiseCollectionProjection.getTotalCollection()));
			}

			if (dateWiseCollectionProjection.getDate().isAfter(oneWeekAgo)) {
				weeklyCollection = weeklyCollection + dateWiseCollectionProjection.getTotalCollection();
			}
			monthlyCollection = monthlyCollection + dateWiseCollectionProjection.getTotalCollection();
		}
		dashboardResponseDto.setWeeklyCollection(BigDecimal.valueOf(weeklyCollection));
		dashboardResponseDto.setMonthlyCollection(BigDecimal.valueOf(monthlyCollection));
		return dashboardResponseDto;
	}

}
