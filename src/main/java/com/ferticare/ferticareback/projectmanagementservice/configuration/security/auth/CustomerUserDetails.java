package com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
public class CustomerUserDetails implements UserDetails {

    private final UUID userId;
    private final String role;

    public CustomerUserDetails(UUID userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    @Override
    public Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> role); // eg: "ADMIN"
    }

    @Override
    public String getPassword() {
        return null; // không cần
    }

    @Override
    public String getUsername() {
        return userId.toString(); // không dùng email ở đây
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return true;
    }
}