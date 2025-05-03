package com.nexoraa.memberiq.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nexoraa.memberiq.utility.ValidationMessages;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ProfileIdValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProfileId {
	String message() default ValidationMessages.ORGANIZATION_ID_REQUIRED;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
