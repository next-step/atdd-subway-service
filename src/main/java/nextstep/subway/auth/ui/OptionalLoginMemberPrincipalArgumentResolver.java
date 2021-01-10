package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.OptionalLoginMember;
import nextstep.subway.auth.domain.OptionalLoginMemberPrincipal;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class OptionalLoginMemberPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public OptionalLoginMemberPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalLoginMemberPrincipal.class);
    }

    @Override
    public OptionalLoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        try {
            return new OptionalLoginMember(authService.findMemberByToken(credentials));
        } catch (AuthorizationException e) {
            return new OptionalLoginMember(null);
        }
    }
}
