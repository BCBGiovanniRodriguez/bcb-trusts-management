package com.bcb.trust.front.model.trusts.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;

public class UserAuthenticated implements UserDetails {

    private CatalogUserEntity systemUser;

    private Collection<? extends GrantedAuthority> authorities;

    public UserAuthenticated() {
    }

    public UserAuthenticated(CatalogUserEntity systemUser) {
        this.systemUser = systemUser;
    }

    public void setSystemUser(CatalogUserEntity systemUser) {
        this.systemUser = systemUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
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
