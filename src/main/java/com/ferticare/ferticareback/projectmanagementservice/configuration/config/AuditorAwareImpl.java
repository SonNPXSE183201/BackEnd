package com.ferticare.ferticareback.projectmanagementservice.configuration.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
<<<<<<< HEAD
        
        if (authentication == null || 
            !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("system");
        }
        
        return Optional.of(authentication.getName());
    }
} 
=======

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("system");
        }

        return Optional.of(authentication.getName());
    }
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
