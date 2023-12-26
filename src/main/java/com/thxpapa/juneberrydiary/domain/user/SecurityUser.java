package com.thxpapa.juneberrydiary.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {

    private final JuneberryUser juneberryUser;

    public SecurityUser(JuneberryUser juneberryUser, Collection<? extends GrantedAuthority> authorities) {
        super(juneberryUser.getEmail(), juneberryUser.getPassword(), authorities);
        this.juneberryUser = juneberryUser;
    }

    public JuneberryUser getJuneberryUser() {
        return juneberryUser;
    }
}
