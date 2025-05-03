package com.nexoraa.memberiq.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexoraa.memberiq.dto.OrganizationDto;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.OrganizationService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/organizations")
public class OrganizationController {

	private final OrganizationService organizationService;

	public OrganizationController(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@PostMapping
	public ResponseEntity<Response> save(@Valid @RequestBody OrganizationDto organizationDto) {
		Response response = new Response();
		try {
			Organization organization = organizationService.save(organizationDto.toOrganization());
			log.info("Organization saved successfully: {}", organization.getId());
			response.addData(organization);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.ORGANIZATION_ADDED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in saving Organization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in saving organization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping
	public ResponseEntity<Response> update(@Valid @RequestBody OrganizationDto organizationDto) {
		Response response = new Response();
		try {
			Organization organization = organizationService.update(organizationDto.toOrganization());
			log.info("Organization Updated successfully: {}", organization.getId());
			response.addData(organization);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.ORGANIZATION_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in Updating Organization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in Updating Organization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/filter")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody List<FilterCriteria> filterCriteria,
			Pageable pageable) {
		Response response = new Response();
		try {
			Page<Organization> organizations = organizationService.BySearchCriteria(filterCriteria, pageable);
			response.addData(organizations);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.ORGANIZATION_FOUND);
		} catch (MemberIqException ex) {
			log.error("Error in getOrganizations: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in getOrganizations: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response> getOrganizationById(@PathVariable("id") UUID id) {
		Response response = new Response();
		try {
			Organization organizations = organizationService.findOrganizationById(id);
			log.info("Get Organization By Id success!!");
			response.addData(organizations);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.ORGANIZATION_FOUND);
		} catch (MemberIqException ex) {
			log.error("Error in get Organization by id: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
		} catch (Exception ex) {
			log.error("Error in get Organization by id: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@PutMapping("/config")
//	public ResponseEntity<Response> updateOrganizationConfig(@RequestBody AccountConfigDto accountConfigDto) {
//		Response response = new Response();
//		try {
//			Organization organization = organizationService.updateOrganizationConfig(accountConfigDto);
//			log.info("Organization Controller: updateOrganizationConfig");
//			response.addData(organization);
//			response.setStatus(HttpStatus.OK.value());
//			response.setStatusMessage(ResponseMessages.ORGANIZATION_CONFIG_UPDATED_SUCCESS);
//		} catch (MemberIqException ex) {
//			log.error("Organization not found : {}", ex.getMessage(), ex);
//			response.setStatusMessage(ex.getMessage());
//			response.setStatus(HttpStatus.NO_CONTENT.value());
//		} catch (Exception ex) {
//			log.error("Error in updateOrganizationConfig: {}", ex.getMessage(), ex);
//			response.setStatusMessage(ex.getMessage());
//			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		}
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@PostMapping("/upload/logo")
	public ResponseEntity<Response> uploadAccountLogo(
			@RequestParam(value = "logo", required = true) MultipartFile logo) {
		Response response = new Response();
		try {

			String fileUrl = organizationService.uploadAccountLogo(logo);
			response.addData(fileUrl);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.LOGO_UPLOAD_SUCESS);
		} catch (Exception ex) {
			log.error("Error in updateOrganizationConfig: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}