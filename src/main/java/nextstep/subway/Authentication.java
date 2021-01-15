package nextstep.subway;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Authentication {
    private final JwtTokenProvider jwtTokenProvider;

    public Authentication(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Around("execution(* nextstep.subway.auth.application.AuthService.findMemberByToken(..))")
    public Object accessCheck(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
            if (returnValue instanceof LoginMember) {
                LoginMember member = (LoginMember)returnValue;
                checkMember(member);
            }
        } catch (Throwable t) {
            throw new AuthorizationException("인증에 실패하였습니다");
        }
        return returnValue;
    }

    private void checkMember(LoginMember member) {
        if (member.getId() == null) {
            throw new RuntimeException();
        }
    }
}
