package com.nexoraa.memberiq.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.nexoraa.memberiq.entity.Membership;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembershipDto {

	private UUID id;

	@NotNull(message = ValidationMessages.DISCOUNT_NOT_NULL)
	@DecimalMin(value = "0.0", inclusive = true, message = ValidationMessages.DISCOUNT_MIN)
	private BigDecimal discount;

	@NotNull(message = ValidationMessages.TOTAL_AMOUNT_NOT_NULL)
	@DecimalMin(value = "0.0", inclusive = false, message = ValidationMessages.TOTAL_AMOUNT_MIN)
	private BigDecimal totalAmount;

	@NotNull(message = ValidationMessages.PAID_AMOUNT_NOT_NULL)
	@DecimalMin(value = "0.0", inclusive = true, message = ValidationMessages.PAID_AMOUNT_MIN)
	private BigDecimal paidAmount;

	@NotNull(message = ValidationMessages.DUE_AMOUNT_NOT_NULL)
	@DecimalMin(value = "0.0", inclusive = true, message = ValidationMessages.DUE_AMOUNT_MIN)
	private BigDecimal dueAmount;

	private ProfileDto member;

	@NotNull(message = ValidationMessages.STATUS_NOT_NULL)
	private Status status;

	@NotNull(message = ValidationMessages.START_DATE_NOT_NULL)
	private LocalDate startDate;

	@NotNull(message = ValidationMessages.END_DATE_NOT_NULL)
	private LocalDate endDate;

	@NotNull(message = ValidationMessages.AUTO_RENEW_NOT_NULL)
	private Boolean autoRenew;

	private OrganizationDto organization;

	private MembershipTypeDto membershipType;

	private GroupDto group;

	public Membership toMembership() {
		return Membership.builder().id(this.id).discount(this.discount).totalAmount(this.totalAmount)
				.paidAmount(this.paidAmount).dueAmount(this.dueAmount).status(this.status).startDate(this.startDate)
				.endDate(this.endDate).autoRenew(this.autoRenew)
				.member(this.member != null ? this.member.toProfile() : null)
				.organization(this.organization != null ? this.organization.toOrganization() : null)
				.membershipType(this.membershipType != null ? this.membershipType.toMembershipType() : null)
				.group(this.group != null ? this.group.toGroup() : null).build();
	}
}