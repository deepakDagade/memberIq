package com.nexoraa.memberiq.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {

	private String token;

	private String newPassword;

}