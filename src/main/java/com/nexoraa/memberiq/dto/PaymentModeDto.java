package com.nexoraa.memberiq.dto;

import java.util.UUID;

import com.nexoraa.memberiq.entity.PaymentMode;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentModeDto {
	private UUID id;
	
	@NotBlank(message = ValidationMessages.PAYMENT_MODE_NAME_REQUIRED)
    private String name;

    @NotBlank(message = ValidationMessages.PAYMENT_MODE_LOGO_URL_REQUIRED)
    private String logoUrl;

    @NotNull(message = ValidationMessages.STATUS_REQUIRED)
    private Status status;
	public PaymentMode toPaymentMode() {

		return PaymentMode.builder().id(id).name(name).logoUrl(logoUrl).status(status).build();
	}
}
