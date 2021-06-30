package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;
    private final UserDetails anonymousUser;

    public AuthenticationPrincipalArgumentResolver(AuthService authService, UserDetails userDetails) {
        this.authService = authService;
        this.anonymousUser = userDetails;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        AuthenticationPrincipal authenticationPrincipal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        if (Objects.isNull(credentials)) {
            checkRequired(authenticationPrincipal);
            return anonymousUser;
        }
        return authService.findMemberByToken(credentials);
    }

    private void checkRequired(AuthenticationPrincipal authenticationPrincipal) {
        if (authenticationPrincipal.isRequired()) {
            throw new AuthorizationException("로그인 필수");
        }
    }
}
