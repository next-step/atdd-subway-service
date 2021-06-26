package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String payload = String.valueOf(member.getId());
        String token = jwtTokenProvider.createToken(payload);
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials, boolean allowAnonymous) {
        boolean invalidToken = !jwtTokenProvider.validateToken(credentials);
        if (invalidToken && allowAnonymous) {
            return LoginMember.ANONYMOUS;
        }
        if (invalidToken) {
            throw new AuthorizationException("유효하지 않은 사용자 입니다.");
        }
        return findMemberByToken(credentials);
    }

    private LoginMember findMemberByToken(String credentials) {
        String payLoad = jwtTokenProvider.getPayload(credentials);
        long memberId = Long.parseLong(payLoad);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalStateException("등록되지 않은 사용자 입니다."));
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
