package com.nexoraa.memberiq.utility;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.nexoraa.memberiq.entity.Organization;
import com.nexoraa.memberiq.exception.MemberIqException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UtilityService {

	public static Organization getUserOrganization() {
		return Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication)
				.filter(Objects::nonNull).map(Authentication::getPrincipal).filter(Jwt.class::isInstance)
				.map(Jwt.class::cast).map(jwtToken -> (String) jwtToken.getClaim("orgId"))
				.filter(orgId -> orgId != null && !orgId.isEmpty()).map(orgId -> {
					Organization organization = new Organization();
					UUID orgUUID = UUID.fromString(orgId);
					organization.setId(orgUUID);
					return organization;
				}).orElseThrow(() -> new MemberIqException(ResponseMessages.ORGANIZATION_NOT_FOUND));
	}

	public static Pageable applySorting(Pageable pageable) {
		if (Objects.isNull(pageable)) {
			throw new IllegalArgumentException(ResponseMessages.PAGEABLE_CANNOT_BE_NULL);
		}

		// Extract the sort field from pageable or set a default
		String sortField = extractSortField(pageable);
		// Extract sorting direction or use a default
		Sort.Direction direction = extractSortDirection(pageable, sortField);

		Sort sort = Sort.by(direction, sortField);

		// Return a new pageable object with the applied sorting
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	private static String extractSortField(Pageable pageable) {
		return pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse("id");
	}

	// Extract direction field from pageable

	private static Sort.Direction extractSortDirection(Pageable pageable, String sortField) {
		return pageable.getSort().getOrderFor(sortField) != null
				? pageable.getSort().getOrderFor(sortField).getDirection()
				: Sort.Direction.DESC;
	}

	public static String getLoggedInUserName() {
		SecurityContext context = SecurityContextHolder.getContext();
		log.info("getLoggedInUserName context: {}", context);
		if (context == null) {
			return null;
		}
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return null;
		}
		try {
			if (authentication.getPrincipal() instanceof String) {
				return (String) authentication.getPrincipal();
			} else if (authentication.getPrincipal() instanceof Jwt) {
				Jwt jwtToken = (Jwt) authentication.getPrincipal();
				return jwtToken.getClaim("user");
			} else {
				// Handle other cases if necessary
				return null;
			}
		} catch (Exception e) {
			log.error("Error While getLogin User From context : {}", e.getMessage());
			throw new MemberIqException("Error While get Login User From context");
		}
	}
}
