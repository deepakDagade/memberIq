package com.nexoraa.memberiq.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.nexoraa.memberiq.utility.ValidationMessages;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.endpoint")
@Data
public class EndpointScopeConfig {

	private Map<String, String> scopes;

    public Map<String, String[]> getEndpointScopes() {
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalStateException(ValidationMessages.ENDPOINT_SCOPES_NOT_FOUND);
        }

        // Convert comma-separated string into String arrays and add "SCOPE_" prefix
        return scopes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> addScopePrefix(entry.getValue().split(","))
                ));
    }

    // Helper method to add "SCOPE_" prefix to each scope
    private String[] addScopePrefix(String[] scopes) {
        return java.util.Arrays.stream(scopes)
                .map(scope -> "SCOPE_" + scope.trim()) // Add "SCOPE_" prefix
                .toArray(String[]::new);
    }

}