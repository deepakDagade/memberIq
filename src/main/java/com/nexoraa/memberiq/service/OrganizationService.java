package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface OrganizationService {

	Organization save(Organization organization);

	Organization update(Organization organization);

	Page<Organization> BySearchCriteria(List<FilterCriteria> filterCriteria, Pageable pageable);

	Organization findOrganizationById(UUID id);

	String uploadAccountLogo(MultipartFile logo);

}
