package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.PaymentDetails;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface PaymentDetailsService {

	PaymentDetails save(PaymentDetails paymentDetails);

	PaymentDetails update(PaymentDetails paymentDetails);

	void bulkUpdate(StatusDto statusDto);

	Page<PaymentDetails> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

}