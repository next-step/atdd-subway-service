package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        try {
            Member member = memberService.findMemberByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
        } catch (NoSuchElementException e) {
            throw new AuthorizationException("존재하지 않는 email입니다.");
        }

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new ForbiddenException("토큰이 유효하지 않습니다.");
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberService.findMemberByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
