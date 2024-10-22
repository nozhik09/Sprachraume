package com.example.Sprachraume.security;

import com.example.Sprachraume.Role.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
public class AuthInfo implements Authentication {

    private boolean authenticated;
    private String email;
    private Set<Role> roles;

    public AuthInfo(String email, Set<Role> roles) {
        this.email = email;
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }


    @Override
    public String getName() {
        return email;
    }
}
