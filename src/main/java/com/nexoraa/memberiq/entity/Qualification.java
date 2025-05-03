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
@Table(name = "Qualifications")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Qualification extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column
	private String degreeName;

	@Column
	private String universityName;

	@Column
	private String passingYear;

	@Column
	private String status;

	@Column
	private String grade;

	@Column
	private Double percentage;

	@Column
	private String remarks;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@OneToOne
	@JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = true)
	private Profile profile;

}
