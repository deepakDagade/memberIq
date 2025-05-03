package com.nexoraa.memberiq.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexoraa.memberiq.entity.PaymentDetails;
import com.nexoraa.memberiq.projection.DateWiseCollectionProjection;
import com.nexoraa.memberiq.projection.GenderWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MembershipWiseCollectionProjection;
import com.nexoraa.memberiq.projection.MonthWiseCollectionProjection;
import com.nexoraa.memberiq.projection.PaymentTypeWiseCollectionProjection;
import com.nexoraa.memberiq.projection.WeekWiseCollectionProjection;

public interface PaymentDetailsRepository
		extends JpaRepository<PaymentDetails, UUID>, JpaSpecificationExecutor<PaymentDetails> {

	Optional<PaymentDetails> findByIdAndOrganizationId(UUID id, UUID id2);

	List<PaymentDetails> findByIdInAndOrganizationId(List<UUID> ids, UUID id);

	@Query("SELECT pm.name AS paymentType, SUM(pd.paymentAmount) AS totalAmount " + "FROM PaymentDetails pd "
			+ "JOIN pd.paymentMode pm " + "WHERE pd.organization.id = :organizationId " + "AND pd.date >=:startDate "
			+ "AND pd.date <=:endDate " + "AND pd.isDeleted = false " + "GROUP BY pm.name " + "ORDER BY pm.name")
	List<PaymentTypeWiseCollectionProjection> getCollectionByPaymentType(@Param("organizationId") UUID organizationId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT p.gender AS gender, SUM(pd.paymentAmount) AS totalAmount"
			+ " FROM PaymentDetails pd JOIN pd.member p"
			+ " WHERE pd.organization.id=:orgId AND pd.date>=:startDate AND pd.date<=:endDate AND pd.isDeleted=false"
			+ " GROUP BY p.gender")
	List<GenderWiseCollectionProjection> getGenderWiseCollection(@Param("orgId") UUID orgId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("""
			SELECT m.membershipType.name AS membershipTypeName,
			       SUM(pd.paymentAmount) AS totalPaidAmount
			FROM PaymentDetails pd
			JOIN pd.member p
			JOIN Membership m ON m.member.id = p.id
			WHERE pd.isDeleted = false
			  AND m.isDeleted = false
			  AND pd.organization.id = :organizationId
			  AND pd.date BETWEEN :startDate AND :endDate
			GROUP BY m.membershipType.name
			""")
	List<MembershipWiseCollectionProjection> getCollectionByMembershipTypeBetweenDates(
			@Param("organizationId") UUID organizationId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT p.date AS date, SUM(p.paymentAmount) AS totalCollection " + "FROM PaymentDetails p "
			+ "WHERE p.date >= :startDate " + "AND  p.date <= :endDate " + "AND p.organization.id = :organizationId "
			+ "GROUP BY p.date " + "ORDER BY p.date ASC")
	List<DateWiseCollectionProjection> findDateWiseCollection(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("organizationId") UUID organizationId);

	@Query(value = "SELECT EXTRACT(WEEK FROM p.date) AS weekNumber, SUM(p.payment_amount) AS totalCollection "
			+ "FROM payment_details p " + "WHERE p.date >= :startDate " + "AND p.date <= :endDate "
			+ "AND p.organization_id = :organizationId " + "GROUP BY weekNumber "
			+ "ORDER BY weekNumber ASC", nativeQuery = true)
	List<WeekWiseCollectionProjection> findWeekWiseCollection(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("organizationId") UUID organizationId);

	@Query(value = "SELECT TO_CHAR(p.date, 'Month') AS monthName, SUM(p.payment_amount) AS totalCollection "
			+ "FROM payment_details p " + "WHERE p.date >= :startDate " + "AND p.date <= :endDate "
			+ "AND p.organization_id = :organizationId " + "GROUP BY monthName "
			+ "ORDER BY MIN(p.date)", nativeQuery = true)
	List<MonthWiseCollectionProjection> findMonthWiseCollection(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("organizationId") UUID organizationId);

	
}
