package com.nexoraa.memberiq.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.nexoraa.memberiq.entity.PaymentDetails;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDetailsDto {

	private UUID id;

	@NotNull(message = ValidationMessages.PAYMENT_MODE_REQUIRED)
	private PaymentModeDto paymentMode;

	@NotNull(message = ValidationMessages.PAYMENT_AMOUNT_REQUIRED)
	private Double paymentAmount;

	@NotNull(message = ValidationMessages.DATE_REQUIRED)
	private LocalDate date;

	@NotBlank(message = ValidationMessages.COMMENT_REQUIRED)
	private String comment;

	@NotNull(message = ValidationMessages.COLLECT_BY_REQUIRED)
	private ProfileDto collectBy;

	@NotNull(message = ValidationMessages.MEMBER_NAME_REQUIRED)
	private ProfileDto member;

	public PaymentDetails toPaymentDetails() {
		return PaymentDetails.builder().id(id).paymentMode(paymentMode.toPaymentMode()).paymentAmount(paymentAmount)
				.date(date).comment(comment).collectBy(collectBy.toProfile()).member(member.toProfile()).build();
	}
}