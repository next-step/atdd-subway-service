package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.ui.FlexibleAuthPrincipalArgumentResolver;
import nextstep.subway.auth.ui.StringAuthPrincipalArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public AuthenticationPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(createStringAuthPrincipalArgumentResolver());
        argumentResolvers.add(createFlexibleAuthPrincipalArgumentResolver());
    }

    @Bean
    public StringAuthPrincipalArgumentResolver createStringAuthPrincipalArgumentResolver() {
        return new StringAuthPrincipalArgumentResolver(authService);
    }

    @Bean
    public FlexibleAuthPrincipalArgumentResolver createFlexibleAuthPrincipalArgumentResolver() {
        return new FlexibleAuthPrincipalArgumentResolver(authService);
    }
}
