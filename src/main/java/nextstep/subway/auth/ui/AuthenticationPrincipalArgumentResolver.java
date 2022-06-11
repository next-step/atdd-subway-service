package nextstep.subway.auth.ui;

import java.util.Objects;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        String credentials = AuthorizationExtractor.extract(
                Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));

        AuthenticationPrincipal authenticationPrincipalAnnotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        if (authenticationPrincipalAnnotation.required()) {
            return authService.findMemberByToken(credentials);
        }

        return authService.findMemberNonRequiredByToken(credentials);
    }
}
