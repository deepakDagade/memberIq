package com.nexoraa.memberiq.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.dto.DashboardResponseDto;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.DashboardService;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/dashobard")
public class DashboardController {

	private DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {

		this.dashboardService = dashboardService;
	}

	@PostMapping("/collection/payment-types")
	public ResponseEntity<Response> getDashboardData() {
		Response response = new Response();
		try {
			DashboardResponseDto dashboardResponseDto = dashboardService.getDashboardData();
			response.addData(dashboardResponseDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.DASHBOARD_DATA_FOUND);
			if (Objects.isNull(dashboardResponseDto)) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.DASHBOARD_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("EError in ReportController.getDashboardData: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ReportController.getDashboardData: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
