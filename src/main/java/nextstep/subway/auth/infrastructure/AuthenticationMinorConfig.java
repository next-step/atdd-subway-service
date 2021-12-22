package nextstep.subway.auth.infrastructure;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.ui.AuthenticationMinorArgumentResolver;

@Configuration
public class AuthenticationMinorConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public AuthenticationMinorConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationMinorArgumentResolver());
    }

    @Bean
    public AuthenticationMinorArgumentResolver createAuthenticationMinorArgumentResolver() {
        return new AuthenticationMinorArgumentResolver(authService);
    }
}
