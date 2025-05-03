package com.nexoraa.memberiq.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.nexoraa.memberiq.utility.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			String username = UtilityService.getLoggedInUserName();
			if (username != null) {
				return Optional.of(username);
			}
		} catch (Exception e) {
			log.error("Error in getCurrentAuditor: {}", e.getMessage());
			e.printStackTrace();
		}
		return Optional.empty();
	}
}