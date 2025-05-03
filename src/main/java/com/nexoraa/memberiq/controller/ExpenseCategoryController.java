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

import com.nexoraa.memberiq.dto.ExpenseCategoryDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.ExpenseCategory;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.ExpenseCategoryService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @PostMapping
    public ResponseEntity<Response> createExpenseCategory(@Valid @RequestBody ExpenseCategoryDto expenseCategoryDto) {
        Response response = new Response();
        try {
            ExpenseCategory createdExpenseCategory = expenseCategoryService.save(expenseCategoryDto.toExpenseCategory());
            response.addData(createdExpenseCategory);
            response.setStatus(HttpStatus.CREATED.value());
            response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORY_CREATED_SUCCESS);
        } catch (MemberIqException ex) {
            log.error("Error in ExpenseCategoryController.createExpenseCategory: {}", ex.getMessage(), ex);
            response.setStatusMessage(ex.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } catch (Exception ex) {
            log.error("Error in ExpenseCategoryController.createExpenseCategory: {}", ex.getMessage(), ex);
            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/filter")
    public ResponseEntity<Response> findBySearchCriteria(@RequestBody List<FilterCriteria> searchCriteria, Pageable pageable) {
        Response response = new Response();
        try {
            Page<ExpenseCategory> expenseCategories = expenseCategoryService.findBySearchCriteria(searchCriteria, pageable);
            response.addData(expenseCategories);
            response.setStatus(HttpStatus.OK.value());
            response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORIES_DATA_FOUND);
            if (expenseCategories.isEmpty()) {
                response.addData(List.of());
                response.setStatus(HttpStatus.NO_CONTENT.value());
                response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORIES_DATA_NOT_FOUND);
            }
        } catch (MemberIqException ex) {
            log.error("Error in ExpenseCategoryController.findBySearchCriteria: {}", ex.getMessage(), ex);
            response.setStatusMessage(ex.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error in ExpenseCategoryController.findBySearchCriteria: {}", ex.getMessage(), ex);
            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> updateExpenseCategory(@Valid @RequestBody ExpenseCategoryDto expenseCategoryDto) {
        Response response = new Response();
        try {
            ExpenseCategory updatedExpenseCategory = expenseCategoryService.update(expenseCategoryDto.toExpenseCategory());
            response.addData(updatedExpenseCategory);
            response.setStatus(HttpStatus.OK.value());
            response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORY_UPDATED_SUCCESS);
        } catch (MemberIqException ex) {
            log.error("Error in ExpenseCategoryController.updateExpenseCategory: {}", ex.getMessage(), ex);
            response.setStatusMessage(ex.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } catch (Exception ex) {
            log.error("Error in ExpenseCategoryController.updateExpenseCategory: {}", ex.getMessage(), ex);
            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/status")
    public ResponseEntity<Response> bulkUpdate(@Valid @RequestBody StatusDto statusDto) {
        Response response = new Response();
        try {
            expenseCategoryService.bulkUpdate(statusDto);
            response.setStatus(HttpStatus.OK.value());
            response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORY_UPDATED_SUCCESS);
        } catch (MemberIqException ex) {
            log.error("Error in ExpenseCategoryController.bulkUpdate: {}", ex.getMessage(), ex);
            response.setStatusMessage(ex.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } catch (Exception ex) {
            log.error("Error in ExpenseCategoryController.bulkUpdate: {}", ex.getMessage(), ex);
            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getExpenseCategoriesByOrganization() {
        Response response = new Response();
        try {
            List<ExpenseCategory> expenseCategories = expenseCategoryService.findByOrganization();
            response.addData(expenseCategories);
            response.setStatus(HttpStatus.OK.value());
            response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORIES_FETCH_SUCCESS);
            if (expenseCategories.isEmpty()) {
                response.addData(List.of());
                response.setStatus(HttpStatus.NO_CONTENT.value());
                response.setStatusMessage(ResponseMessages.EXPENSE_CATEGORIES_DATA_NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Error in ExpenseCategoryController.getExpenseCategoriesByOrganization: {}", ex.getMessage(), ex);
            response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}