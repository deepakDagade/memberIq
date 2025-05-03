package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.enums.EnquiryType;
import com.nexoraa.memberiq.utility.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "FrontOffice")
@EqualsAndHashCode(callSuper = false)
@ToString
public class FrontOffice extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String phoneNo;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private EnquiryType enquiryType;

	@Column
	private LocalDate enquiryDate;

	@Column
	private LocalDate followUpDate;

	@Column
	private LocalDate nextFollowUpDate;

	@Column
	private String source;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	@JsonIgnore
	private Organization organization;

	@Column
	@JsonIgnore
	private Boolean isDeleted;
}
