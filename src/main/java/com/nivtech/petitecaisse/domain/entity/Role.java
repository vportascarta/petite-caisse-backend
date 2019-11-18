package com.nivtech.petitecaisse.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
    ROLE_CASH,
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_EXTERNAL;

    @Override
    public String getAuthority()
    {
        return name();
    }
}
