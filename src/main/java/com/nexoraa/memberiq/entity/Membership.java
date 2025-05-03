package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Memberships")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Membership extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column
	private BigDecimal discount;

	@Column
	private BigDecimal totalAmount;

	@Column
	private BigDecimal paidAmount;

	@Column
	private BigDecimal dueAmount;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Profile member;

	@Column
	private Status status;

	@Column
	private LocalDate startDate;

	@Column
	private LocalDate endDate;

	@Column
	private Boolean autoRenew;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	@JsonIgnore
	private Organization organization;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "membership_type_id")
	private MembershipType membershipType;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

}
