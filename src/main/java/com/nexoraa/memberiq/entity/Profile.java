package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.enums.Gender;
import com.nexoraa.memberiq.enums.ProfileType;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "profiles")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Profile extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String middleName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = true)
	private String phoneNumber;

	@Column
	private String profileImageUrl;

	@Column
	private String aadherNo;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ProfileType profileType;

	@Column
	private LocalDate dateOfBirth;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@OneToOne(mappedBy = "profile")
	private Qualification qualification;

	@OneToOne(mappedBy = "profile")
	private BankDetails bankDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	@JsonIgnore
	private Organization organization;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "profile_groups", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	@JsonIgnore
	private List<Group> groups;

}
