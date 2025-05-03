package com.nexoraa.memberiq.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Membership;
import com.nexoraa.memberiq.specification.FilterCriteria;

public interface MembershipService {

    Membership save(Membership membership);

    Membership update(Membership membership);

    void bulkUpdate(StatusDto statusDto);

    Page<Membership> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

    List<Membership> findByOrganization();
}