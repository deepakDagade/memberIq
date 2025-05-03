package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.enums.OrganizationType;
import com.nexoraa.memberiq.enums.Status;
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
import jakarta.persistence.OneToMany;
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
@Table(name = "organizations")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Organization extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column
	private String logoUrl;

	@Column(nullable = false)
	private String contactNumber;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private OrganizationType type;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<AppUser> users;

	@OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Group> groups;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

}