package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "MembershipType")
public class MembershipType extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private BigDecimal minCost;

	@Column(nullable = false)
	private BigDecimal maxCost;

	@Column
	private String duration;

	@Column
	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	@JsonIgnore
	private Organization organization;

	@Column
	@JsonIgnore
	private Boolean isDeleted;
}