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

import com.nexoraa.memberiq.dto.PaymentModeDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.PaymentMode;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.PaymentModeService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/payment-modes")
public class PaymentModeController {

	private final PaymentModeService paymentModeService;

	public PaymentModeController(PaymentModeService paymentModeService) {
		this.paymentModeService = paymentModeService;
	}

	@PostMapping
	public ResponseEntity<Response> createPaymentMode(@Valid @RequestBody PaymentModeDto paymentModeDto) {
		Response response = new Response();
		try {
			PaymentMode createdPaymentMode = paymentModeService.save(paymentModeDto.toPaymentMode());
			response.addData(createdPaymentMode);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_MODE_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentModeController.createPaymentMode: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentModeController.createPaymentMode: {}", ex.getMessage(), ex);
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
			Page<PaymentMode> paymentModes = paymentModeService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(paymentModes);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_MODES_DATA_FOUND);
			if (paymentModes.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PAYMENT_MODES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in PaymentModeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in PaymentModeController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updatePaymentMode(@Valid @RequestBody PaymentModeDto paymentModeDto) {
		Response response = new Response();
		try {
			PaymentMode updatedPaymentMode = paymentModeService.update(paymentModeDto.toPaymentMode());
			response.addData(updatedPaymentMode);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_MODE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentModeController.updatePaymentMode: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentModeController.updatePaymentMode: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getPaymentModes() {
		Response response = new Response();
		try {
			List<PaymentMode> paymentModes = paymentModeService.findAll();
			response.addData(paymentModes);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_MODES_FETCH_SUCCESS);

			if (paymentModes.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PAYMENT_MODES_DATA_NOT_FOUND);
			}
		} catch (Exception ex) {
			log.error("Error in PaymentModeController.getPaymentModes: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/bulk-update-status")
	public ResponseEntity<Response> bulkUpdateStatus(@RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			paymentModeService.bulkUpdateStatus(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_MODE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentModeController.bulkUpdateStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentModeController.bulkUpdateStatus: {}", ex.getMessage(), ex);
			response.setStatusMessage("Internal server error.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}