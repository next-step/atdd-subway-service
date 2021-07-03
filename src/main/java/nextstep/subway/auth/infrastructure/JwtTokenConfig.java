package nextstep.subway.auth.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.DefaultJwtParser;

@Configuration
public class JwtTokenConfig {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Bean
    public JwtParser createJwtParser() {
        return new DefaultJwtParser().setSigningKey(this.secretKey);
    }
}
