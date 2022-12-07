package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.domain.AnonymousMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        if(isAnonymousMember(parameter, credentials)) {
            return new AnonymousMember();
        }

        return authService.findMemberByToken(credentials);
    }

    private boolean isAnonymousMember(MethodParameter parameter, String credentials) {
        AuthenticationPrincipal principal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        if (!principal.required() && StringUtils.isEmpty(credentials)) {
            return true;
        }

        return false;
    }
}
