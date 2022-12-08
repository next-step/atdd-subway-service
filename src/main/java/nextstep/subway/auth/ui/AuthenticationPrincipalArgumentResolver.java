package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        AuthenticationPrincipal authenticationPrincipal = parameter.getParameterAnnotation(
                AuthenticationPrincipal.class);
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));

        assert authenticationPrincipal != null;
        if (credentials == null && !authenticationPrincipal.required()) {
            return LoginMember.none();
        }
        return authService.findMemberByToken(credentials);
    }
}
