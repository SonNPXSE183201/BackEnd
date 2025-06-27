package com.ferticare.ferticareback.projectmanagementservice.configuration.security.aspect;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.CustomerUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ManagerOnlyAspect {

    @Pointcut("@annotation(com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly)")
    public void managerOnlyMethods() {}

    @Before("managerOnlyMethods()")
    public void checkManagerRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomerUserDetails userDetails)) {
            throw new RuntimeException("User not found");
        }

        if (!"MANAGER".equals(userDetails.getRole())) {
            throw new RuntimeException("Access denied: Manager only");
        }
    }
}