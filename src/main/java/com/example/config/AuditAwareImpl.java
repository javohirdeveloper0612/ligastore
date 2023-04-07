package com.example.config;

import com.example.entity.ProfileEntity;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AuditAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                || !Objects.requireNonNull(authentication).getPrincipal().equals("anonymousUser")) {
            return Optional.of((UUID) authentication.getPrincipal());
        }

        return Optional.empty();
    }
}
