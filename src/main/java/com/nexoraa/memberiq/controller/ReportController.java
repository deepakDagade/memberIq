package com.nexoraa.memberiq.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.projection.DateWiseCollectionProjection;
import com.nexoraa.memberiq.projection.GenderWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MembershipWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MonthWiseCollectionProjection;
import com.nexoraa.memberiq.projection.PaymentTypeWiseCollectionProjection;
import com.nexoraa.memberiq.projection.WeekWiseCollectionProjection;
import com.nexoraa.memberiq.service.ReportService;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;

	}

	@GetMapping("/collection/payment-types")
	public ResponseEntity<Response> FindyCollectionByPaymentType(
			@RequestParam(name = "organizationId", required = false) UUID organizationId,
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) LocalDate endDate) {
		Response response = new Response();
		try {
			List<PaymentTypeWiseCollectionProjection> paymentTypeWiseCollections = reportService
					.FindyCollectionByPaymentType(organizationId, startDate, endDate);
			response.addData(paymentTypeWiseCollections);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (paymentTypeWiseCollections.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.findCollectionByPaymentType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.findCollectionByPaymentType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/collection/gender")
	public ResponseEntity<Response> getGenderWiseCollection(
			@RequestParam(name = "organizationId", required = false) UUID organizationId,
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) LocalDate endDate) {
		Response response = new Response();
		try {
			List<GenderWiseCollectionProjection> genderWiseCollectionProjection = reportService
					.getGenderWiseCollection(organizationId, startDate, endDate);
			response.addData(genderWiseCollectionProjection);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (genderWiseCollectionProjection.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.findCollectionByPaymentType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.findCollectionByPaymentType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/collection/membership-type")
	public ResponseEntity<Response> getCollectionByMembershipType(
			@RequestParam(name = "organizationId", required = true) UUID organizationId,
			@RequestParam(name = "startDate", required = true) LocalDate startDate,
			@RequestParam(name = "endDate", required = true) LocalDate endDate) {
		Response response = new Response();
		try {
			List<MembershipWiseCollectionProjection> membershipWiseCollectionProjection = reportService
					.getCollectionByMembershipType(organizationId, startDate, endDate);
			response.addData(membershipWiseCollectionProjection);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (membershipWiseCollectionProjection.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.getCollectionByMembershipType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.getCollectionByMembershipType: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/collection/date-wise")
	public ResponseEntity<Response> getDateWiseCollection(
			@RequestParam(name = "organizationId", required = true) UUID organizationId,
			@RequestParam(name = "startDate", required = true) LocalDate startDate,
			@RequestParam(name = "endDate", required = true) LocalDate endDate) {
		Response response = new Response();
		try {
			List<DateWiseCollectionProjection> dateWiseCollectionProjection = reportService
					.getDateWiseCollection(organizationId, startDate, endDate);
			response.addData(dateWiseCollectionProjection);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (dateWiseCollectionProjection.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.getDateWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.getDateWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/collection/week-wise")
	public ResponseEntity<Response> getWeekWiseCollection(
			@RequestParam(name = "organizationId", required = false) UUID organizationId,
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) LocalDate endDate) {
		Response response = new Response();
		try {
			List<WeekWiseCollectionProjection> weekWiseCollectionProjection = reportService
					.getWeekWiseCollection(organizationId, startDate, endDate);
			response.addData(weekWiseCollectionProjection);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (weekWiseCollectionProjection.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.getWeekWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.getWeekWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/collection/month-wise")
	public ResponseEntity<Response> getMonthWiseCollection(
			@RequestParam(name = "organizationId", required = false) UUID organizationId,
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) LocalDate endDate) {
		Response response = new Response();
		try {
			List<MonthWiseCollectionProjection> membershipWiseCollectionProjection = reportService
					.getMonthWiseCollection(organizationId, startDate, endDate);
			response.addData(membershipWiseCollectionProjection);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.PROFILES_FOUND);
			if (membershipWiseCollectionProjection.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.PROFILES_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.getMonthWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.getMonthWiseCollection: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
