/*
package com.example.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                || !Objects.requireNonNull(authentication).getPrincipal().equals("anonymousUser")) {
            return Optional.of((Long) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
*/
