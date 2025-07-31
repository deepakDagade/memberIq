package com.nexoraa.memberiq.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.dto.MembershipTypeDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.MembershipType;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.MembershipTypeService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/membership-types")
public class MembershipTypeController {

	private final MembershipTypeService membershipTypeService;

	public MembershipTypeController(MembershipTypeService membershipTypeService) {
		this.membershipTypeService = membershipTypeService;
	}

	@PostMapping
	public ResponseEntity<Response> createMembershipType(@RequestBody MembershipTypeDto membershipTypeDto) {
		Response response = new Response();
		try {
			MembershipType membershipType = membershipTypeService.save(membershipTypeDto.toMembershipType());
			response.addData(membershipType);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPE_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error creating membership type: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Unexpected error: {}", ex.getMessage(), ex);
			response.setStatusMessage("Internal server error.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping
	public ResponseEntity<Response> updateMembershipType(@RequestBody MembershipTypeDto membershipTypeDto) {
		Response response = new Response();
		try {
			MembershipType updatedMembershipType = membershipTypeService.update(membershipTypeDto.toMembershipType());
			response.addData(updatedMembershipType);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error updating membership type: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Unexpected error: {}", ex.getMessage(), ex);
			response.setStatusMessage("Internal server error.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/filter")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody List<FilterCriteria> searchCriteria,
			Pageable pageable) {
		Response response = new Response();
		try {
			Page<MembershipType> membershipTypes = membershipTypeService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(membershipTypes);
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPES_FETCH_SUCCESS);
			response.setStatus(HttpStatus.FOUND.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPES_FETCH_SUCCESS);
			if (membershipTypes.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPE_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in MembershipTypeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in MembershipTypeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setData(List.of());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/bulk-update")
	public ResponseEntity<Response> BulkUpdateStatus(@RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			membershipTypeService.BulkUpdateStatus(statusDto);
			response.setStatus(HttpStatus.NO_CONTENT.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error deleting membership type: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Unexpected error: {}", ex.getMessage(), ex);
			response.setStatusMessage("Internal server error.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getAllMembershipTypes() {
		Response response = new Response();
		try {
			List<MembershipType> membershipTypes = membershipTypeService.findAll();
			response.addData(membershipTypes);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_TYPES_FETCH_SUCCESS);
			if (membershipTypes.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage("No membership types available.");
			}
		} catch (Exception ex) {
			log.error("Error fetching membership types: {}", ex.getMessage(), ex);
			response.setStatusMessage("Internal server error.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}