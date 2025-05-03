package com.nexoraa.memberiq.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentTypeWiseCollectionDto {
	private String paymentType;
	private BigDecimal totalAmount;

	public PaymentTypeWiseCollectionDto(String paymentType, BigDecimal totalAmount) {

		this.paymentType = paymentType;
		this.totalAmount = totalAmount;
	}

}
