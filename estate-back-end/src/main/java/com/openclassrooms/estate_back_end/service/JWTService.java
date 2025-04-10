package com.openclassrooms.estate_back_end.service;

import com.openclassrooms.estate_back_end.configuration.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {

    private final JwtEncoder jwtEncoder;

    private final JwtProperties jwtProperties;

    public JWTService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String subject = authentication.getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.DAYS))
            .subject(subject)
            .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
            .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        try {
            return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        }
        catch (Exception e) {
            throw new RuntimeException("Token generation failed. Please try again later.");
        }
    }

}
