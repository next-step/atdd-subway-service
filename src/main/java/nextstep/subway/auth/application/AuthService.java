package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@Service
public class AuthService {
    public static final String INVALID_TOKEN = "인증정보가 유효하지 않습니다.";
    private static final String ERROR_INVALID_EMAIL_OR_PASSWORD = "이메일 또는 비밀번호가 올바르지 않습니다.";
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = findByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return LoginMember.empty();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = findByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new AuthorizationException(ERROR_INVALID_EMAIL_OR_PASSWORD));
    }
}
