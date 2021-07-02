package nextstep.subway.auth.infrastructure;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private JwtParser jwtParser;

    @Autowired
    public JwtTokenProvider(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(validityInMilliseconds, ChronoUnit.MILLIS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token) {
        return this.jwtParser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return !this.jwtParser
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(toDate(LocalDateTime.now()));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

