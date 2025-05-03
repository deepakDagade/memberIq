package com.nexoraa.memberiq.validation;

import java.util.Objects;

import com.nexoraa.memberiq.dto.ProfileDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProfileIdValidator implements ConstraintValidator<ValidProfileId, ProfileDto> {

	private String message;

	@Override
	public void initialize(final ValidProfileId constraintAnnotation) {
		this.message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final ProfileDto profileDto, ConstraintValidatorContext context) {
		boolean isValid = Objects.nonNull(profileDto) && Objects.nonNull(profileDto.getId());
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		}
		return isValid;
	}

}
