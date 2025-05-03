package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.utility.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BankDetails")
@EqualsAndHashCode(callSuper = false)
@ToString
public class BankDetails extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column
	private String accountHolderName;

	@Column
	private String bankName;

	@Column
	private String branchName;

	@Column
	private String ifscCode;

	@Column
	private String accountNo;

	@Column
	private String panNo;

	@Column
	private String accountType;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@OneToOne
	@JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = true)
	private Profile profile;
}
