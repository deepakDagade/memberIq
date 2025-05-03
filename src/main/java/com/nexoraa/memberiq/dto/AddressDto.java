package com.nexoraa.memberiq.dto;

import java.util.UUID;

import com.nexoraa.memberiq.entity.Address;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDto {
	private UUID id;

	@NotNull(message = ValidationMessages.ADDRESS_REQUIRED)
	private String addressName;

	
	@NotBlank(message = ValidationMessages.STREET_REQUIRED)
	private String street;

	@NotBlank(message = ValidationMessages.CITY_REQUIRED)
	private String city;

	@NotBlank(message = ValidationMessages.STATE_REQUIRED)
	private String state;

	@NotBlank(message = ValidationMessages.POSTAL_CODE_REQUIRED)
	private String postalCode;

	@NotBlank(message = ValidationMessages.COUNTRY_REQUIRED)
	private String country;

	public Address toAddress() {
		return Address.builder().addressName(addressName).street(street).city(city).state(state).postalCode(postalCode)
				.country(country).build();
	}
}