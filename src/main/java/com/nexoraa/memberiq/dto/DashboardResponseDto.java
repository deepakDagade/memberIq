package com.nexoraa.memberiq.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DashboardResponseDto {

	private Long activeMember;

	private Long todayPlanExpiry;

	private Long todayRenewal;

	private Long monthlyRenewal;

	private Long weeklyRenewal;

	private BigDecimal todaysCollection;

	private BigDecimal weeklyCollection;

	private BigDecimal monthlyCollection;

	private Long memberoverDues;

	private BigDecimal todaysExpence;

	private BigDecimal monthlyExpence;

	private BigDecimal weeklyExcepence;

	private BigDecimal todaysEnquery;

	private BigDecimal weeklyEnquery;

	private BigDecimal monthlyEnquery;

}
