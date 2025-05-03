package com.nexoraa.memberiq.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.dto.PaymentDetailsDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.PaymentDetails;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.PaymentDetailsService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/payment-details")
public class PaymentDetailsController {

	private final PaymentDetailsService paymentDetailsService;

	public PaymentDetailsController(PaymentDetailsService paymentDetailsService) {
		this.paymentDetailsService = paymentDetailsService;
	}

	@PostMapping
	public ResponseEntity<Response> createPaymentDetail(@Valid @RequestBody PaymentDetailsDto paymentDetailsDto) {
		Response response = new Response();
		try {
			PaymentDetails createdPaymentDetail = paymentDetailsService.save(paymentDetailsDto.toPaymentDetails());
			response.addData(createdPaymentDetail);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_DETAIL_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentDetailsController.createPaymentDetail: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentDetailsController.createPaymentDetail: {}", ex.getMessage(), ex);
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
			Page<PaymentDetails> paymentDetailsPage = paymentDetailsService.findBySearchCriteria(searchCriteria,
					pageable);
			response.addData(paymentDetailsPage);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_DETAILS_DATA_FOUND);
			if (paymentDetailsPage.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PAYMENT_DETAILS_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in PaymentDetailsController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			log.error("Error in PaymentDetailsController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updatePaymentDetail(@Valid @RequestBody PaymentDetailsDto paymentDetailsDto) {
		Response response = new Response();
		try {
			PaymentDetails updatedPaymentDetail = paymentDetailsService.update(paymentDetailsDto.toPaymentDetails());
			response.addData(updatedPaymentDetail);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_DETAIL_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentDetailsController.updatePaymentDetail: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentDetailsController.updatePaymentDetail: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/status")
	public ResponseEntity<Response> bulkUpdate(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			paymentDetailsService.bulkUpdate(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PAYMENT_DETAIL_STATUS_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in PaymentDetailsController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in PaymentDetailsController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

}