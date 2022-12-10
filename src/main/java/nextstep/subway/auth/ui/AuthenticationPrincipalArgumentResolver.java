package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;

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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        AuthenticationPrincipal authenticationPrincipal = parameter.getParameterAnnotation(
            AuthenticationPrincipal.class);
        if (isPathFindAndNoToken(credentials, authenticationPrincipal)) {
            return LoginMember.GUEST;
        }
        return authService.findMemberByToken(credentials);
    }

    private boolean isPathFindAndNoToken(String credentials, AuthenticationPrincipal authenticationPrincipal) {
        return !authenticationPrincipal.required() && !StringUtils.hasText(credentials);
    }
}
