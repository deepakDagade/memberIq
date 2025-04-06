package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column
	private String name;

	@JsonIgnore
	@Builder.Default
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@ToString.Exclude
	private Set<AppUser> users = new HashSet<>();

	@JsonManagedReference
	@Builder.Default
	@ManyToMany
	@JoinTable(name = "role_scope", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "scope_id"))
	@ToString.Exclude
	private Set<Scope> scopes = new HashSet<>();
}