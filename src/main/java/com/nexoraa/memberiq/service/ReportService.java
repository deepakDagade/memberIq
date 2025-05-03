package com.nexoraa.memberiq.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.nexoraa.memberiq.projection.DateWiseCollectionProjection;
import com.nexoraa.memberiq.projection.GenderWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MembershipWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MonthWiseCollectionProjection;
import com.nexoraa.memberiq.projection.PaymentTypeWiseCollectionProjection;
import com.nexoraa.memberiq.projection.WeekWiseCollectionProjection;

public interface ReportService {

	List<PaymentTypeWiseCollectionProjection> FindyCollectionByPaymentType(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

	List<GenderWiseCollectionProjection> getGenderWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

	List<MembershipWiseCollectionProjection> getCollectionByMembershipType(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

	List<DateWiseCollectionProjection> getDateWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

	List<WeekWiseCollectionProjection> getWeekWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

	List<MonthWiseCollectionProjection> getMonthWiseCollection(UUID organizationId, LocalDate startDate,
			LocalDate endDate);

}
