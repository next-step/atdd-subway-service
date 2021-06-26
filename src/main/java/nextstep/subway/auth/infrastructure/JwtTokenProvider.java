package nextstep.subway.auth.infrastructure;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        Base64.Encoder encoder = Base64.getEncoder();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, encoder.encode(secretKey.getBytes()))
                .compact();
    }

    public String getPayload(String token) {
        Base64.Encoder encoder = Base64.getEncoder();
        return Jwts.parser()
                .setSigningKey(encoder.encode(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(encoder.encode(secretKey.getBytes()))
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

