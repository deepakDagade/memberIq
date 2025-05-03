package com.nexoraa.memberiq.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.dto.ProfileDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Profile;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.ProfileService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/profiles")
public class ProfileController {

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@PostMapping
	public ResponseEntity<Response> addProfile(@Valid @RequestBody ProfileDto profileDto) {
		Response response = new Response();
		try {
			Profile savedProfile = profileService.save(profileDto.toProfile());
			log.info("Profile added successfully: {}", savedProfile.getId());
			response.addData(savedProfile);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.PROFILE_ADDED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ProfileController.addProfile: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ProfileController.addProfile: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping
	public ResponseEntity<Response> updateProfile(@Valid @RequestBody ProfileDto profileDto) {
		Response response = new Response();
		try {
			Profile updatedProfile = profileService.update(profileDto.toProfile());
			log.info("Profile updated successfully: {}", updatedProfile.getId());
			response.addData(updatedProfile);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ProfileController.updateProfile: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ProfileController.updateProfile: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/filter")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody List<FilterCriteria> searchCriteria,
			Pageable pageable) {
		Response response = new Response();
		try {
			Page<Profile> profiles = profileService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(profiles);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (profiles.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in ProfileController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ProfileController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/status")
	public ResponseEntity<Response> updateProfileStatus(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			profileService.updateProfileStatus(statusDto);
			log.info("Profile status updated successfully");
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ProfileController.updateProfileStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ProfileController.updateProfileStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getProfilesByOrgId() {
		Response response = new Response();
		try {
			List<Profile> profiles = profileService.getProfilesByOrgId();
			response.addData(profiles);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (profiles.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in ProfileController.getProfilesByOrgId: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());

		} catch (Exception ex) {
			log.error("Error in ProfileController.getProfilesByOrgId: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}