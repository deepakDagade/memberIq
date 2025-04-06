package com.nexoraa.memberiq.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexoraa.memberiq.utility.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	private String imageUrl;

	@Column
	@JsonIgnore
	private Boolean isDeleted;

	@Column(nullable = false)
	private String contactNumber;

	@JsonIgnore
	@Builder.Default
	@ManyToMany(mappedBy = "organizations", fetch = FetchType.LAZY)
	@ToString.Exclude
	private Set<AppUser> users = new HashSet<>();
}