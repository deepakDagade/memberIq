package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.MembershipType;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface MembershipTypeService {
	MembershipType save(MembershipType membershipType);

	List<MembershipType> findAll();

	MembershipType update(MembershipType membershipType);

	void BulkUpdateStatus(StatusDto statusDto);

	Page<MembershipType> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);
}