package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexoraa.memberiq.entity.Scope;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, UUID> {
	@Query("SELECT s FROM Scope s JOIN s.appUsers a WHERE a.id=:appUserId")
	List<Scope> findByUserId(@Param("appUserId") UUID appUserId);

}