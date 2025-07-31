package com.nexoraa.memberiq.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexoraa.memberiq.entity.PaymentMode;
import com.nexoraa.memberiq.enums.Status;

public interface PaymentModeRepository extends JpaRepository<PaymentMode, UUID>, JpaSpecificationExecutor<PaymentMode> {

	Optional<PaymentMode> findByNameAndOrganizationIdAndIsDeletedFalse(String name, UUID organizationId);

	@Query("SELECT p FROM PaymentMode p WHERE (p.id=:id OR p.name= :name) AND p.organization.id = :organizationId AND p.isDeleted = false")
	List<PaymentMode> findByIdOrNameAndOrganizationIdAndIsDeletedFalse(@Param("id") UUID id, @Param("name") String name,
			@Param("organizationId") UUID organizationId);

	void findByOrganizationIdAndStatusAndIsDeletedFalse(UUID organizationId, Status active);

	List<PaymentMode> findByIdInAndOrganizationIdAndIsDeletedFalse(List<UUID> ids, UUID organizationId);

	PaymentMode findByNameAndIsDeletedFalse(String membershipPaymentType);

}
