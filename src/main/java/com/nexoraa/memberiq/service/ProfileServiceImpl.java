package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.entity.Profile;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.ProfileRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.ProfileSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	public ProfileServiceImpl(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@Transactional
	@Override
	public Profile save(Profile profile) {
		log.info("Saving profile: {}", profile.getFirstName());

		// Set the organization
		Organization organization = UtilityService.getUserOrganization();

		validateProfile(profile, organization.getId());

		profile.setOrganization(organization);
		// validate profile duplicate phone number

		profile.setIsDeleted(Boolean.FALSE);
		Profile savedProfile = profileRepository.save(profile);
		log.info("Profile saved successfully: {}", savedProfile.getId());
		return savedProfile;
	}

	private void validateProfile(Profile profile, UUID organizationId) {
		Optional<Profile> profiles = profileRepository
				.findByPhoneNumberAndOrganizationIdAndIsDeletedFalse(profile.getPhoneNumber(), organizationId);
		if (profiles.isPresent()) {
			throw new MemberIqException(
					String.format(ResponseMessages.PHONE_NUMBER_ALREADY_REGISTERED, profile.getPhoneNumber()));
		}
	}

	@Transactional
	@Override
	public Profile update(Profile profile) throws MemberIqException {
		log.info("Updating profile: {}", profile.getId());
		Organization organization = UtilityService.getUserOrganization();
		Profile dbProfile = validateUniquePhoneNumber(profile, organization.getId());

		// Copy fields
		copyProfileFields(dbProfile, profile);

		Profile updatedProfile = profileRepository.save(dbProfile);
		log.info("Profile updated successfully: {}", updatedProfile.getId());
		return updatedProfile;
	}

	private Profile validateUniquePhoneNumber(Profile profile, UUID organizationId) throws MemberIqException {

		List<Profile> profiles = profileRepository.findByIdOrPhoneNumberAndOrganizationIdAndIsDeletedFalse(
				profile.getId(), profile.getPhoneNumber(), organizationId);

		if (!profiles.isEmpty()) {
			if (profiles.size() > GlobalConstants.ONE) {
				for (Profile dbProfile : profiles) {
					if (!profile.getId().equals(dbProfile.getId())) {
						throw new MemberIqException(String.format(ResponseMessages.PHONE_NUMBER_ALREADY_REGISTERED,
								profile.getPhoneNumber()));
					}
				}
			}

		} else {
			throw new MemberIqException(ResponseMessages.PROFILE_NOT_FOUND);
		}
		return profiles.get(GlobalConstants.ZERO);
	}

	private void copyProfileFields(Profile target, Profile source) {
		target.setFirstName(source.getFirstName());
		target.setMiddleName(source.getMiddleName());
		target.setLastName(source.getLastName());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setGender(source.getGender());
		target.setStatus(source.getStatus());
		target.setProfileImageUrl(source.getProfileImageUrl());
		target.setProfileType(source.getProfileType());
		target.setDateOfBirth(source.getDateOfBirth());
		target.setAddress(source.getAddress());
	}

	@Override
	public Page<Profile> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching profiles with criteria: {}", searchCriteria);
		Organization organization = UtilityService.getUserOrganization();
		Specification<Profile> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);
		
		return profileRepository.findAll(combinedSpec, pageable);

	}

	private Specification<Profile> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {
		List<Specification<Profile>> specs = searchCriteria.stream().map(criteria -> new ProfileSpecification(criteria))
				.collect(Collectors.toList());

		specs.add(new ProfileSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new ProfileSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));
		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Transactional
	@Override
	public void updateProfileStatus(StatusDto statusDto) {

		Organization organization = UtilityService.getUserOrganization();

		List<Profile> profiles = profileRepository.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(),
				organization.getId());
		if (profiles.isEmpty()) {
			throw new MemberIqException(ResponseMessages.PROFILES_NOT_FOUND);
		}

		if (statusDto.getStatus().equals(Status.DELETED)) {
			profiles.forEach(profile -> profile.setIsDeleted(true));
		} else {
			profiles.forEach(profile -> profile.setStatus(statusDto.getStatus()));
		}

		profileRepository.saveAll(profiles);
	}

	@Override
	public List<Profile> getProfilesByOrgId() {

		Organization organization = UtilityService.getUserOrganization();

		return profileRepository.findByOrganizationIdAndIsDeletedFalse(organization.getId());

	}
}
