package nextstep.subway.auth.ui;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.subway.auth.application.AuthService;
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
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String credentials = AuthorizationExtractor.extract(
			Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
		if (isGuest(parameter, credentials)) {
			return LoginMember.guest();
		}
		return authService.findMemberByToken(credentials);
	}

	private boolean isGuest(MethodParameter parameter, String credentials) {
		AuthenticationPrincipal authenticationPrincipal =
			parameter.getParameterAnnotation(AuthenticationPrincipal.class);

		return isGuest(credentials, authenticationPrincipal);
	}

	private boolean isGuest(String credentials, AuthenticationPrincipal authenticationPrincipal) {
		return authenticationPrincipal != null && authenticationPrincipal.guestAllowed() && credentials == null;
	}
}
