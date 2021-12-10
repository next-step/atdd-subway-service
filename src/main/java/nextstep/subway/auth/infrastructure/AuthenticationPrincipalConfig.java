package nextstep.subway.auth.infrastructure;

import java.util.*;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

import nextstep.subway.auth.application.*;
import nextstep.subway.auth.ui.*;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final AuthReadService authReadService;

    public AuthenticationPrincipalConfig(AuthService authService, AuthReadService authReadService) {
        this.authService = authService;
        this.authReadService = authReadService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService, authReadService);
    }
}
