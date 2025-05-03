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

import com.nexoraa.memberiq.dto.GroupDto;
import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.service.GroupService;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.utility.Response;
import com.nexoraa.memberiq.utility.ResponseMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/admin/groups")
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@PostMapping
	public ResponseEntity<Response> createGroup(@Valid @RequestBody GroupDto groupDto) {
		Response response = new Response();
		try {
			Group createdGroup = groupService.save(groupDto.toGroup());
			response.addData(createdGroup);
			response.setStatus(HttpStatus.CREATED.value());
			response.setStatusMessage(ResponseMessages.GROUP_CREATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in GroupController.createGroup: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in GroupController.createGroup: {}", ex.getMessage(), ex);
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
			Page<Group> groups = groupService.findBySearchCriteria(searchCriteria, pageable);
			response.addData(groups);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.GROUPS_DATA_FOUND);
			if (groups.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.GROUP_DATA_NOT_FOUND);
			}
		} catch (MemberIqException ex) {
			log.error("Error in GroupController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Error in GroupController.findBySearchCriteria: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Response> updateGroup(@Valid @RequestBody GroupDto groupDto) {
		Response response = new Response();
		try {
			Group updatedGroup = groupService.update(groupDto.toGroup());
			response.addData(updatedGroup);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.GROUP_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in GroupController.updateGroup: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in GroupController.updateGroup: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PutMapping("/status")
	public ResponseEntity<Response> bulkUpdate(@Valid @RequestBody StatusDto statusDto) {
		Response response = new Response();
		try {
			groupService.bulkUpdate(statusDto);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.GROUP_UPDATED_SUCCESS);
		} catch (MemberIqException ex) {
			log.error("Error in GroupController.deleteGroup: {}", ex.getMessage(), ex);
			response.setStatusMessage(ex.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (Exception ex) {
			log.error("Error in GroupController.deleteGroup: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/all")
	public ResponseEntity<Response> getGroupsByOrganization() {
		Response response = new Response();
		try {
			List<Group> groups = groupService.findByOrganization();

			response.addData(groups);
			response.setStatus(HttpStatus.OK.value());
			response.setStatusMessage(ResponseMessages.GROUPS_FETCH_SUCCESS);

			if (groups.isEmpty()) {
				response.addData(List.of());
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setStatusMessage(ResponseMessages.GROUP_DATA_NOT_FOUND);
			}
		} catch (Exception ex) {
			log.error("Error in GroupController.getGroupsByOrganization: {}", ex.getMessage(), ex);
			response.setStatusMessage(ResponseMessages.INTERNAL_SERVER_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}