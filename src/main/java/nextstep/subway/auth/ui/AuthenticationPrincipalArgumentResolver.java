package nextstep.subway.auth.ui;

import javax.servlet.http.*;

import org.springframework.core.*;
import org.springframework.web.bind.support.*;
import org.springframework.web.context.request.*;
import org.springframework.web.method.support.*;

import nextstep.subway.auth.application.*;
import nextstep.subway.auth.domain.*;
import nextstep.subway.auth.infrastructure.*;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final AuthReadService authReadService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService, AuthReadService authReadService) {
        this.authService = authService;
        this.authReadService = authReadService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        return authReadService.findMemberByToken(credentials);
    }
}
