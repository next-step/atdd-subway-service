package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;

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
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        LoginMember member = authService.findMemberByToken(credentials);

        if (parameter.hasParameterAnnotation(Valid.class)) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, member, "resolvedObjectLogicalName");
            checkValidation(binder);
        }

        return member;
    }

    private void checkValidation(WebDataBinder binder) {
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new AuthorizationException();
        }
    }
}
