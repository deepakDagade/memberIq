package com.nexoraa.memberiq.dto;

import java.util.UUID;

import com.nexoraa.memberiq.entity.Qualification;

import lombok.Data;

@Data
public class QualificationDto {
	private UUID id;
	private String degreeName;
	private String universityName;
	private String passingYear;
	private String status;
	private String grade;
	private Double percentage;
	private String remarks;

	public Qualification toQualification() {
		return Qualification.builder().id(id).degreeName(degreeName).universityName(universityName)
				.passingYear(passingYear).status(status).grade(grade).percentage(percentage).remarks(remarks).build();
	}
}
