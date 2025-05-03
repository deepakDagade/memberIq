package com.nexoraa.memberiq.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.nexoraa.memberiq.entity.FrontOffice;
import com.nexoraa.memberiq.enums.EnquiryType;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class FrontOfficeDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;

	private String name;
	private String email;
	private String phoneNo;
	private EnquiryType enquiryType;
	private LocalDate enquiryDate;
	private LocalDate followUpDate;
	private LocalDate nextFollowUpDate;
	private String source;

	@Valid
	private AddressDto address;

	private String comment;

	public FrontOffice toFrontOffice() {
		return FrontOffice.builder().id(id).name(name).email(email).phoneNo(phoneNo).enquiryType(enquiryType)
				.address(Objects.nonNull(address) ? address.toAddress() : null).enquiryDate(enquiryDate)
				.comment(comment).followUpDate(followUpDate).nextFollowUpDate(nextFollowUpDate).source(source).build();
	}
}
