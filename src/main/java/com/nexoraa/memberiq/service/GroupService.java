package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.specification.FilterCriteria;

import jakarta.validation.Valid;

public interface GroupService {
	Group save(Group group);

	Group update(Group group);

	void bulkUpdate(@Valid StatusDto statusDto);

	List<Group> findByOrganization();

	Page<Group> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

}
