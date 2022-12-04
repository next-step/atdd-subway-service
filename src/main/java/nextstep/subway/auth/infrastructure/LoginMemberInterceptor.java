package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class LoginMemberInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginMemberInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String credentials = AuthorizationExtractor.extract(request);
        LoginMember loginMember = LoginMember.ANONYMOUS;

        if (Objects.nonNull(credentials)) {
            loginMember = authService.findMemberByToken(credentials);
        }
        LoginMemberThreadLocal.set(loginMember);

        return true;
    }
}
