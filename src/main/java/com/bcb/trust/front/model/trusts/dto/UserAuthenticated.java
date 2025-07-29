package com.bcb.trust.front.model.trusts.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;


public class UserAuthenticated implements UserDetails {

    private SystemUserEntity systemUser;

    public UserAuthenticated() {
    }

    public UserAuthenticated(SystemUserEntity systemUser) {
        this.systemUser = systemUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("Admin")); // @TODO Change!!
    }

    @Override
    public String getPassword() {
        return systemUser.getAccess();
    }

    @Override
    public String getUsername() {
        return systemUser.getNickname();
    }
}
