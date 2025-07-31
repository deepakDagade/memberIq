package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.PaymentMode;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface PaymentModeService {

	PaymentMode save(PaymentMode paymentMode);

	Page<PaymentMode> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

	PaymentMode update(PaymentMode paymentMode);

	List<PaymentMode> findAll();

	void bulkUpdateStatus(StatusDto statusDto);

	 PaymentMode getDefaultPaymentMode();
}