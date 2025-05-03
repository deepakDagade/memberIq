package com.nexoraa.memberiq.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.UserRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.UserSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public AppUser updateUserLastLogin(String email) {
		log.info("UserServiceImpl: updateUserLastLogin: {}", email);
		AppUser user = userRepository.findByEmailAndIsDeletedFalse(email);
		if (Objects.isNull(user)) {
			throw new RuntimeErrorException(null, "User not found");
		}
		// userRepository.updateLastLogin(email, LocalDateTime.now());
		return user;
	}

	@Override
	@Transactional
	public AppUser save(AppUser appUser) {
		log.info("Saving user: {}", appUser.getEmail());

		// Set login user organizations
		Organization organization = UtilityService.getUserOrganization();
		appUser.setOrganization(organization);

		validateUserEmailExist(appUser);

		appUser.setPassword(encryptPassword(appUser.getPassword()));
		// appUser.setStatus(Status.UNVERIFIED);
		appUser.setStatus(Status.ACTIVE);
		appUser.setResetPasswordToken(UUID.randomUUID().toString());
		appUser.setTokenExpiry(LocalDateTime.now().plusHours(GlobalConstants.TOKEN_EXPIRATION_HOURS));

		appUser.setIsDeleted(Boolean.FALSE);
		AppUser savedUser = userRepository.save(appUser);
		log.info("User saved successfully: {}", savedUser.getId());
		return savedUser;
	}

	@Override
	public Page<AppUser> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching users with criteria: {}", searchCriteria);

		return findAllUsers(searchCriteria, pageable);

	}

	private Page<AppUser> findAllUsers(List<FilterCriteria> filterCriteria, Pageable pageable) {
		log.info("findAllUsers()");

		// Use the organization from the payload if provided, otherwise fetch the user's
		// organization
		Organization organization = UtilityService.getUserOrganization();

		Specification<AppUser> combinedSpec = createSpecifications(filterCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);

		log.info("findAllUsers filterCriteria: {}", filterCriteria);
		return userRepository.findAll(combinedSpec, pageable);
	}

	private Specification<AppUser> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {

		List<Specification<AppUser>> specs = searchCriteria.stream().map(criteria -> new UserSpecification(criteria))
				.collect(Collectors.toList());

		specs.add(new UserSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new UserSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));
		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	@Transactional
	public void updateUserStatus(@Valid StatusDto statusDto) {
		// Method for bulk updating user statuses
		List<AppUser> users = userRepository.findByIdIn(statusDto.getIds());
		if (users.isEmpty()) {
			throw new MemberIqException(ResponseMessages.USER_NOT_FOUND);
		}
		users.forEach(user -> user.setStatus(statusDto.getStatus()));
		userRepository.saveAll(users);
	}

	@Override
	public void deleteUsers(List<UUID> userIds) {
		log.info("Deleting users by IDs: {}", userIds);
		List<AppUser> users = userRepository.findByIdIn(userIds);

		if (users.isEmpty()) {
			throw new MemberIqException(ResponseMessages.USER_NOT_FOUND);
		}

		users.forEach(user -> user.setIsDeleted(true));
		userRepository.saveAll(users);
	}

	@Override
	public Page<AppUser> getUsersByOrgId(UUID orgId, Pageable pageable) {
		return userRepository.findByOrganizationIdAndIsDeletedFalse(orgId, pageable);

	}

	private String encryptPassword(String password) {
		if (Objects.nonNull(password)) {
			return passwordEncoder.encode(password);
		}
		return password;
	}

	private void validateUserEmailExist(AppUser user) {
		log.info("Validating email existence for: {}", user.getEmail());
		Optional<AppUser> existingUser = userRepository.findByEmailAndOrganizationIdAndIsDeletedFalse(user.getEmail(),
				user.getOrganization().getId());
		if (existingUser.isPresent()) {
			throw new MemberIqException(ResponseMessages.EMAIL_ALREADY_EXIST);
		}
	}

	@Override
	public void generatePasswordResetToken(String email, String type) {
		log.info("generatePasswordResetToken email: {}", email);
		AppUser user = userRepository.findByEmailAndIsDeletedFalse(email);
		if (Objects.nonNull(user)) {
			String token = UUID.randomUUID().toString();
			user.setResetPasswordToken(token);
			user.setTokenExpiry(LocalDateTime.now().plusHours(GlobalConstants.TOKEN_EXPIRATION_HOURS));
			AppUser saveUser = userRepository.save(user);
//			EmailDetails emailDetails = new EmailDetails();
//			emailDetails.setToEmail(saveUser.getEmail());
//			emailDetails.setToken(token);
//			emailDetails.setType(type);
//			log.info("sendResetPasswordMail emai: {}", email);
//			emailService.sendResetPasswordMail(emailDetails);
		} else {
			log.error("User not found in generatePasswordResetToken: {}", email);
		}
	}

	@Override
	public void resetPassword(String token, String newPassword) {
		AppUser appUser = userRepository.findByResetPasswordTokenAndIsDeletedFalse(token);
		if (Objects.isNull(appUser)) {
			log.error("ResetPassword: {}", ResponseMessages.ERROR_INVALID_TOKEN);
			throw new MemberIqException(ResponseMessages.ERROR_INVALID_TOKEN);
		}
		if (isTokenExpired(appUser.getTokenExpiry())) {
			log.error("ResetPassword: {}", ResponseMessages.TOKEN_EXPIRED);
			throw new MemberIqException(ResponseMessages.TOKEN_EXPIRED);
		}
		appUser.setPassword(passwordEncoder.encode(newPassword));
		appUser.setStatus(Status.ACTIVE);
		appUser.setResetPasswordToken(null);
		appUser.setTokenExpiry(null);
		userRepository.save(appUser);
	}

	private boolean isTokenExpired(LocalDateTime tokenExpiry) {
		return tokenExpiry.isBefore(LocalDateTime.now());
	}
	
}
