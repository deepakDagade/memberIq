package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.enums.Status;
import com.nexoraa.memberiq.utility.Auditable;

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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
@ToString
public class AppUser extends Auditable<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = true)
	private String phoneNumber;

	@JsonIgnore
	@Column(nullable = true)
	private String password;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = true)
	private String resetPasswordToken;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@Column(nullable = true)
	private LocalDateTime tokenExpiry;

	@ManyToMany(fetch = FetchType.EAGER)
	@Builder.Default
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@ToString.Exclude
	private List<Role> roles = new ArrayList<>();

	@Column
	private LocalDateTime lastLogin;

	@ManyToMany(fetch = FetchType.EAGER)
	@Builder.Default
	@JoinTable(name = "user_organization", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "org_id"))
	@ToString.Exclude
	private List<Organization> organizations = new ArrayList<>();

	// for external use
	@Column(nullable = true)
	private String userId;

	@Transient
	private Organization organization;

	@Column
	private String profileImageUrl;
}