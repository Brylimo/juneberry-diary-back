package com.thxpapa.juneberrydiary.security.provider;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY="BBERRYlskdfdkjsljfsdlkjfsldjflskfdjlskjfdlsjdflksjfdkdjksdjflksdjfksjfjsldfjsldkfjslkdfjlskdfjlskdfjlsjfklsdjflkjsdlfkjslkdfjslkdjflksdjflksjdfklsdjfklsjdfkljvnlskdfljks";

    public String create(JuneberryUser juneberryUser) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(String.valueOf(juneberryUser.getJuneberryUserUid()))
                .setIssuer("juneberry diary app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
