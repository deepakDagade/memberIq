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

import com.nexoraa.memberiq.dto.MembershipDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Membership;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.MembershipService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/memberships")
public class MembershipController {

	private final MembershipService membershipService;

	public MembershipController(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	@PostMapping
	public ResponseEntity<Response> createMembership(@Valid @RequestBody MembershipDto membershipDto) {
		Response response = new Response();
		try {
			Membership createdMembership = membershipService.save(membershipDto.toMembership());
			response.addData(createdMembership);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in MembershipController.createMembership: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in MembershipController.createMembership: {}", ex.getMessage(), ex);
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
			Page<Membership> memberships = membershipService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(memberships);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIPS_DATA_FOUND);
			if (memberships.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.MEMBERSHIP_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in MembershipController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in MembershipController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updateMembership(@Valid @RequestBody MembershipDto membershipDto) {
		Response response = new Response();
		try {
			Membership updatedMembership = membershipService.update(membershipDto.toMembership());
			response.addData(updatedMembership);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in MembershipController.updateMembership: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in MembershipController.updateMembership: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/status")
	public ResponseEntity<Response> bulkUpdateStatus(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			membershipService.bulkUpdate(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIP_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in MembershipController.bulkUpdateStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in MembershipController.bulkUpdateStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getMembershipsByOrganization() {
		Response response = new Response();
		try {
			List<Membership> memberships = membershipService.findByOrganization();
			response.addData(memberships);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.MEMBERSHIPS_FETCH_SUCCESS);
			if (memberships.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.MEMBERSHIP_DATA_NOT_FOUND);
			}
		} catch (Exception ex) {
			log.error("Error in MembershipController.getMembershipsByOrganization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}