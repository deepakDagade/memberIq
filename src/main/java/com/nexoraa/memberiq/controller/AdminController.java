package com.nexoraa.memberiq.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.dto.UserDTO;
import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.UserService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/admin")
public class AdminController {

	private final UserService userService;

	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/add-user")
	public ResponseEntity<Response> addUser(@Valid @RequestBody UserDTO userDTO) {
		Response response = new Response();
		try {
			AppUser savedUser = userService.save(userDTO.toAppUser());
			log.info("addUser successfully: {}", savedUser.getId());
			response.addData(savedUser);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.USER_ADDED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in AdminController.addUser: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in AdminController.addUser: {}", ex.getMessage(), ex);
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
			Page<AppUser> users = userService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(users);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.USER_DATA_FOUND);
			if (users.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.USER_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in AdminController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in AdminController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/status")
	public ResponseEntity<Response> updateUserStatus(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			userService.updateUserStatus(statusDto);
			log.info("User Status updated successfully");
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.USER_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in AdminController.updateUserStatus(): {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in AdminController.updateUserStatus(): {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete-users")
	public ResponseEntity<Response> deleteUsers(@RequestBody UUID[] ids) {
		Response response = new Response();
		try {
			List<UUID> userIds = Arrays.asList(ids);
			log.info("AdminController.deleteUsers Ids: {}", userIds);
			if (userIds.isEmpty()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				response.setStatusMessage(ResponseMessages.INVALID_REQUEST);
			} else {
				userService.deleteUsers(userIds);
				response.setStatus(HttpStatus.OK.value());
				response.setStatusMessage(ResponseMessages.USER_DELETE_SUCCESS);
			}
		} catch (MemberIqException ex) {
			log.error("Error in AdminController.deleteUsers: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in AdminController.deleteUsers: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/organization/{id}")
	public ResponseEntity<Response> getUsersByOrgId(@PathVariable("id") UUID orgId, Pageable pageable) {
		Response response = new Response();
		try {
			Page<AppUser> users = userService.getUsersByOrgId(orgId, pageable);
			response.addData(users);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.USER_FOUND);
			if (users.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.USER_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in AdminController.getUsersByOrgId: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.OK.value());
		} catch (Exception ex) {
			log.error("Error in AdminController.getUsersByOrgId: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/user/{email}")
	public ResponseEntity<Response> getUsersByEmail(@PathVariable("email") String email) {
		Response response = new Response();
		try {
			AppUser user = userService.getUserByEmail(email);
			response.addData(user);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.USER_FOUND);
			if (Objects.isNull(user)) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				response.setStatusMessage(ResponseMessages.USER_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (MemberIqException ex) {
			log.error("Error in AdminController.getUsersByEmail: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} catch (Exception ex) {
			log.error("Error in AdminController.getUsersByEmail: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}