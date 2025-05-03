package com.nexoraa.memberiq.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.entity.Profile;
import com.nexoraa.memberiq.enums.Gender;
import com.nexoraa.memberiq.enums.ProfileType;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;

	@NotBlank(message = ValidationMessages.FIRST_NAME_REQUIRED)
	private String firstName;

	@NotBlank(message = ValidationMessages.MIDDLE_NAME_REQUIRED)
	private String middleName;

	@NotBlank(message = ValidationMessages.LAST_NAME_REQUIRED)
	private String lastName;

	@NotBlank(message = ValidationMessages.PHONE_NUMBER_REQUIRED)
	private String phoneNumber;

	@NotNull(message = ValidationMessages.GENDER_REQUIRED)
	private Gender gender;

	@NotNull(message = ValidationMessages.STATUS_REQUIRED)
	private Status status;

	private String profileImageUrl;

	private String aadherNo;

	@NotNull(message = ValidationMessages.PROFILE_TYPE_REQUIRED)
	private ProfileType profileType;

	@NotNull(message = ValidationMessages.DATE_OF_BIRTH_REQUIRED)
	private LocalDate dateOfBirth;

	@Valid
	private AddressDto address;

	@Valid
	private OrganizationDto organization;

	private List<GroupDto> groups;

	private QualificationDto qualification;

	private BankDetailsDto bankDetails;

	public Profile toProfile() {
		Profile profile = Profile.builder().id(id).firstName(firstName).middleName(middleName).lastName(lastName)
				.phoneNumber(phoneNumber).gender(gender).status(status).profileImageUrl(profileImageUrl)
				.aadherNo(aadherNo).profileType(profileType).dateOfBirth(dateOfBirth)
				.address(Objects.nonNull(address) ? address.toAddress() : null)
				.qualification(Objects.nonNull(qualification) ? qualification.toQualification() : null)
				.bankDetails(Objects.nonNull(bankDetails) ? bankDetails.toBankDetails() : null)
				.organization(Objects.nonNull(organization) ? organization.toOrganization() : null).build();

		if (Objects.nonNull(groups) && !groups.isEmpty()) {
			List<Group> groupList = groups.stream().map(groupDto -> groupDto.toGroup()).collect(Collectors.toList());
			profile.setGroups(groupList);
		} else {
			profile.setGroups(List.of());
		}

		return profile;
	}

}