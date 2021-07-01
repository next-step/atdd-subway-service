package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final UserDetails userDetails;

    public AuthenticationPrincipalConfig(AuthService authService, UserDetails userDetails) {
        this.authService = authService;
        this.userDetails = userDetails;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService, userDetails.getUserDetails());
    }
}
