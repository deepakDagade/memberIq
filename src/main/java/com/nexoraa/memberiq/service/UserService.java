package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.specification.FilterCriteria;

import jakarta.validation.Valid;

public interface UserService {

	AppUser updateUserLastLogin(String name);

	AppUser save(AppUser appUser);

	Page<AppUser> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable);

	void updateUserStatus(@Valid StatusDto statusDto);

	void deleteUsers(List<UUID> userIds);

	Page<AppUser> getUsersByOrgId(UUID orgId, Pageable pageable);

	void generatePasswordResetToken(String email, String type);

	void resetPassword(String token, String newPassword);

}
