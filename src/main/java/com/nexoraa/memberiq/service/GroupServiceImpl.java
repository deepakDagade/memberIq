package com.nexoraa.memberiq.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexoraa.memberiq.dto.StatusDto;
import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.exception.MemberIqException;
import com.nexoraa.memberiq.repository.GroupRepository;
import com.nexoraa.memberiq.specification.FilterCriteria;
import com.nexoraa.memberiq.specification.GroupSpecification;
import com.nexoraa.memberiq.utility.GlobalConstants;
import com.nexoraa.memberiq.utility.ResponseMessages;
import com.nexoraa.memberiq.utility.UtilityService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;

	public GroupServiceImpl(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Override
	public Group save(Group group) {

		Organization organization = UtilityService.getUserOrganization();

		group.setOrganization(organization);
		// validate group duplicate name in same org not allowed
		validateGroupName(group.getName(), organization.getId());
		group.setIsDeleted(Boolean.FALSE);

		Group createdGroup = groupRepository.save(group);
		return createdGroup;
	}

	private void validateGroupName(String groupName, UUID organizationId) {
		Optional<Group> dbGroup = groupRepository.findByNameAndOrganizationIdAndIsDeletedFalse(groupName,
				organizationId);
		if (dbGroup.isPresent()) {
			throw new MemberIqException(String.format(ResponseMessages.GROUP_NAME_ALREADY_EXIST, groupName));
		}
	}

	@Override
	public Group update(Group group) {

		Organization organization = UtilityService.getUserOrganization();

		group.setOrganization(organization);

		// validate group duplicate name in same org not allowed
		Group dbGroup = validateGroupName(group.getId(), group.getName(), organization.getId());

		dbGroup.setName(group.getName());
		dbGroup.setDescription(group.getDescription());
		dbGroup.setStatus(group.getStatus());

		return groupRepository.save(dbGroup);
	}

	private Group validateGroupName(UUID id, String groupName, UUID organizationId) {
		List<Group> groups = groupRepository.findByIdOrNameAndOrganizationIdAndIsDeletedFalse(id, groupName,
				organizationId);

		if (groups.isEmpty()) {
			throw new MemberIqException(ResponseMessages.GROUP_NOT_FOUND);
		}
		if (groups.size() > GlobalConstants.ONE) {
			for (Group group : groups) {
				if (!id.equals(group.getId())) {
					if (groupName.equalsIgnoreCase(group.getName())) {
						throw new MemberIqException(
								String.format(ResponseMessages.GROUP_NAME_ALREADY_EXIST, groupName));
					}
				}
			}
		}
		return groups.get(GlobalConstants.ZERO);
	}

	@Override
	public Page<Group> findBySearchCriteria(List<FilterCriteria> searchCriteria, Pageable pageable) {
		log.info("Searching groups with criteria: {}", searchCriteria);

		Organization organization = UtilityService.getUserOrganization();

		Specification<Group> combinedSpec = createSpecifications(searchCriteria, organization.getId());

		pageable = UtilityService.applySorting(pageable);

		return groupRepository.findAll(combinedSpec, pageable);

	}

	private Specification<Group> createSpecifications(List<FilterCriteria> searchCriteria, UUID organizationId) {
		List<Specification<Group>> specs = searchCriteria.stream().map(GroupSpecification::new)
				.collect(Collectors.toList());

		specs.add(new GroupSpecification(new FilterCriteria(GlobalConstants.IS_DELETED, ":", Boolean.FALSE)));

		specs.add(new GroupSpecification(new FilterCriteria(GlobalConstants.ORGANIZATION, ":", organizationId)));

		return specs.stream().reduce(Specification::and).orElse(null);
	}

	@Override
	public List<Group> findByOrganization() {

		Organization organization = UtilityService.getUserOrganization();

		return groupRepository.findByOrganizationIdAndIsDeletedFalse(organization.getId());
	}

	@Override
	public void bulkUpdate(@Valid StatusDto statusDto) {

		Organization organization = UtilityService.getUserOrganization();

		List<Group> groups = groupRepository.findByIdInAndOrganizationIdAndIsDeletedFalse(statusDto.getIds(),
				organization.getId());
		if (groups.isEmpty()) {
			throw new MemberIqException(ResponseMessages.GROUP_DATA_NOT_FOUND);
		}
		if (statusDto.getStatus().equals(Status.DELETED)) {
			groups.forEach(group -> group.setIsDeleted(true));
		} else {
			groups.forEach(group -> group.setStatus(statusDto.getStatus()));
		}
		groupRepository.saveAll(groups);
	}
}