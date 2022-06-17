package nextstep.subway.auth.ui;

import java.util.Objects;
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
    private final AuthService authService;

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
        AuthenticationPrincipal parameterAnnotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        String credentials = AuthorizationExtractor.extract(
                Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        if (!Objects.requireNonNull(parameterAnnotation).required() && credentials == null) {
            return new LoginMember();
        }
        return authService.findMemberByToken(credentials);
    }
}
