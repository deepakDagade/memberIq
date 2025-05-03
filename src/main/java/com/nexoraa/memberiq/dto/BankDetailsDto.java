package com.nexoraa.memberiq.dto;

import java.io.Serializable;
import java.util.UUID;

import com.nexoraa.memberiq.entity.BankDetails;

import lombok.Data;

@Data
public class BankDetailsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;

	private String accountHolderName;
	private String bankName;
	private String branchName;
	private String ifscCode;
	private String accountNo;
	private String panNo;
	private String accountType;

	public BankDetails toBankDetails() {
		return BankDetails.builder().id(id).accountHolderName(accountHolderName).bankName(bankName)
				.branchName(branchName).ifscCode(ifscCode).accountNo(accountNo).panNo(panNo).accountType(accountType)
				.build();
	}
}
