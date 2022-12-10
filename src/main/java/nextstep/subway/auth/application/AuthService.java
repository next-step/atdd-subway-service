package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.DiscountPolicy;
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

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials, boolean required) {
        shouldBeValidTokenRequired(credentials, required);
        if (invalidToken(credentials)) {
            return LoginMember.guest();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge(),
                DiscountPolicy.fromAge(member.getAge()));
    }

    private void shouldBeValidTokenRequired(String credentials, boolean required) {
        if (invalidToken(credentials) && required) {
            throw new AuthorizationException("유효하지 않은 인증 정보입니다");
        }
    }

    private boolean invalidToken(String credentials) {
        return !jwtTokenProvider.validateToken(credentials);
    }
}
