package com.nexoraa.memberiq.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.nexoraa.memberiq.entity.AppUser;
import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

	private UUID id;

	@Email(message = ValidationMessages.EMAIL_INVALID)
	@NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
	private String email;

	private String password;

	private List<String> roles;

	private LocalDateTime tokenExpiry;

	private ProfileDto profile;

	public AppUser toAppUser() {
		return AppUser.builder().id(id).email(email).password(password).roles(null).tokenExpiry(tokenExpiry)
				.profile(Objects.nonNull(profile) ? profile.toProfile() : null).build();
	}
}