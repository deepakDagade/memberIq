package com.nexoraa.memberiq.dto;

import java.util.List;
import java.util.UUID;

import com.nexoraa.memberiq.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

	private List<UUID> ids;

	private Status status;

//	@ValidUserOrganizationId
//	private OrganizationDto organization;
}
