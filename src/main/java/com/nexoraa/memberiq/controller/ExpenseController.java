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

import com.nexoraa.memberiq.dto.ExpenseDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Expense;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.ExpenseService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@PostMapping
	public ResponseEntity<Response> createExpense(@Valid @RequestBody ExpenseDto expenseDto) {
		Response response = new Response();
		try {
			Expense createdExpense = expenseService.save(expenseDto.toExpense());
			response.addData(createdExpense);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.EXPENSE_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ExpenseController.createExpense: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ExpenseController.createExpense: {}", ex.getMessage(), ex);
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
			Page<Expense> expenses = expenseService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(expenses);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.EXPENSES_DATA_FOUND);
			if (expenses.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.EXPENSE_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in ExpenseController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in ExpenseController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updateExpense(@Valid @RequestBody ExpenseDto expenseDto) {
		Response response = new Response();
		try {
			Expense updatedExpense = expenseService.update(expenseDto.toExpense());
			response.addData(updatedExpense);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.EXPENSE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ExpenseController.updateExpense: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ExpenseController.updateExpense: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/status")
	public ResponseEntity<Response> bulkUpdate(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			expenseService.bulkUpdate(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.EXPENSE_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in ExpenseController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in ExpenseController.bulkUpdate: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

}