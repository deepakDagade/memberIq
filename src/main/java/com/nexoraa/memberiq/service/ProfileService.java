package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Profile;
import com.nexoraa.memberiq.specification.FilterCriteria;

import jakarta.validation.Valid;

public interface ProfileService {

	Profile save(Profile profile);

	Profile update(Profile profile);

	Page<Profile> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

	void updateProfileStatus(@Valid StatusDto statusDto);

	List<Profile> getProfilesByOrgId();

}
