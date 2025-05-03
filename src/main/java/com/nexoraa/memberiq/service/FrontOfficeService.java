package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.FrontOffice;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface FrontOfficeService {

	FrontOffice save(FrontOffice frontOffice);

	FrontOffice update(FrontOffice frontOffice);

	void bulkUpdate(StatusDto statusDto);

	Page<FrontOffice> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

	List<FrontOffice> findByOrganization();
}