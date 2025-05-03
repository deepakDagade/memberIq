package com.nexoraa.memberiq.dto;

import java.util.Objects;
import java.util.UUID;

import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.enums.OrganizationType;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationDto {
	private UUID id;
	@NotBlank(message = ValidationMessages.ORG_NAME_REQUIRED)
	private String name;

	@NotBlank(message = ValidationMessages.LOGO_URL_REQUIRED)
	private String logoUrl;

	@NotBlank(message = ValidationMessages.CONTACT_NUMBER_REQUIRED)
	private String contactNumber;

	@NotNull(message = ValidationMessages.STATUS_REQUIRED)
	private Status status;

	@NotNull(message = ValidationMessages.ORG_TYPE_REQUIRED)
	private OrganizationType type;

	private AddressDto address;

	public Organization toOrganization() {
		return Organization.builder().id(id).name(name).logoUrl(logoUrl).contactNumber(contactNumber).status(status)
				.type(type).address(Objects.nonNull(address) ? address.toAddress() : null).build();
	}
}
