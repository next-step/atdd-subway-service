package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private static final String EMAIL_NOT_FOUND = "회원 이메일을 찾을 수 없습니다";
    private static final String INVALID_CREDENTIALS = "invalid credentials : ";
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        final Member member = getMember(request.getEmail());
        member.checkPassword(request);

        final String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(INVALID_CREDENTIALS + credentials);
        }
        final String email = jwtTokenProvider.getPayload(credentials);
        final Member member = getMember(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException(EMAIL_NOT_FOUND));
    }
}
