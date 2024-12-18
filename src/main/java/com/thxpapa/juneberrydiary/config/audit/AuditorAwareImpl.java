package com.thxpapa.juneberrydiary.config.audit;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        JuneberryUser user = (JuneberryUser)authentication.getPrincipal();
        return Optional.of(user.getJuneberryUserUid());
    }
}