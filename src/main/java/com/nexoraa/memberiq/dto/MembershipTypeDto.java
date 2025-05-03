package com.nexoraa.memberiq.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.nexoraa.memberiq.entity.MembershipType;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembershipTypeDto {
	private UUID id;

	@NotBlank(message = ValidationMessages.MEMBERSHIP_NAME_REQUIRED)
	private String name;

	@NotNull(message = ValidationMessages.MIN_COST_REQUIRED)
	private BigDecimal minCost;

	@NotNull(message = ValidationMessages.MAX_COST_REQUIRED)
	private BigDecimal maxCost;

	@NotBlank(message = ValidationMessages.DURATION_REQUIRED)
	private String duration;

	@NotNull(message = ValidationMessages.STATUS_REQUIRED)
	private Status status;

	public MembershipType toMembershipType() {
		return MembershipType.builder().id(id).name(name).minCost(minCost).maxCost(maxCost).duration(duration)
				.status(status).build();
	}
}