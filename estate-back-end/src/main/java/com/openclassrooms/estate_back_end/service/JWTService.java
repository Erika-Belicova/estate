package com.openclassrooms.estate_back_end.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.openclassrooms.estate_back_end.configuration.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
	
	private JwtEncoder jwtEncoder;
	private JwtProperties jwtProperties;

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

		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(),
				claims
		);
		return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}

}
