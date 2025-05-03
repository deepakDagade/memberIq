package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "PaymentDetails")
@EqualsAndHashCode(callSuper = false)
@ToString
public class PaymentDetails extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private Double paymentAmount;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private String comment;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "payment_mode_id", nullable = false)
	private PaymentMode paymentMode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "collected_by_id", nullable = false)
	private Profile collectBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "member_id", nullable = false)
	private Profile member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	@JsonIgnore
	private Organization organization;
}