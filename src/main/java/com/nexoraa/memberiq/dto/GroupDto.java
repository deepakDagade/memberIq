package com.nexoraa.memberiq.dto;

import java.io.Serializable;
import java.util.UUID;

import com.nexoraa.memberiq.entity.Group;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.ValidationMessages;

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
public class GroupDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;
	@NotBlank(message = ValidationMessages.GROUP_NAME_REQUIRED)
	private String name;

	private String description;

	@NotNull(message = ValidationMessages.STATUS_REQUIRED)
	private Status status;

	public Group toGroup() {
		return Group.builder().id(id).name(name).description(description).status(status).build();

	}
}
