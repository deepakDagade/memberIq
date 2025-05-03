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

import com.nexoraa.memberiq.dto.FrontOfficeDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.FrontOffice;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.FrontOfficeService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/front-offices")
public class FrontOfficeController {

	private final FrontOfficeService frontOfficeService;

	public FrontOfficeController(FrontOfficeService frontOfficeService) {
		this.frontOfficeService = frontOfficeService;
	}

	@PostMapping
	public ResponseEntity<Response> createFrontOffice(@Valid @RequestBody FrontOfficeDto frontOfficeDto) {
		Response response = new Response();
		try {
			FrontOffice createdFrontOffice = frontOfficeService.save(frontOfficeDto.toFrontOffice());
			response.addData(createdFrontOffice);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.FRONT_OFFICE_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in FrontOfficeController.createFrontOffice: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in FrontOfficeController.createFrontOffice: {}", ex.getMessage(), ex);
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
			Page<FrontOffice> frontOffices = frontOfficeService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(frontOffices);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.FRONT_OFFICES_DATA_FOUND);
			if (frontOffices.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.FRONT_OFFICES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in FrontOfficeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in FrontOfficeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updateFrontOffice(@Valid @RequestBody FrontOfficeDto frontOfficeDto) {
		Response response = new Response();
		try {
			FrontOffice updatedFrontOffice = frontOfficeService.update(frontOfficeDto.toFrontOffice());
			response.addData(updatedFrontOffice);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.FRONT_OFFICE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in FrontOfficeController.updateFrontOffice: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in FrontOfficeController.updateFrontOffice: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/status")
	public ResponseEntity<Response> bulkUpdate(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			frontOfficeService.bulkUpdate(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.FRONT_OFFICE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in FrontOfficeController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in FrontOfficeController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getFrontOfficesByOrganization() {
		Response response = new Response();
		try {
			List<FrontOffice> frontOffices = frontOfficeService.findByOrganization();

			response.addData(frontOffices);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.FRONT_OFFICES_FETCH_SUCCESS);

			if (frontOffices.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.FRONT_OFFICES_DATA_NOT_FOUND);
			}
		} catch (Exception ex) {
			log.error("Error in FrontOfficeController.getFrontOfficesByOrganization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}