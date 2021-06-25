package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.ui.RequestUserArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class RequestUserConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public RequestUserConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createRequestUserArgumentResolver());
    }

    @Bean
    public RequestUserArgumentResolver createRequestUserArgumentResolver() {
        return new RequestUserArgumentResolver(authService);
    }
}
