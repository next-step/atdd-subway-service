package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.exception.AuthorizationException;
import nextstep.subway.member.domain.Email;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String ERROR_MESSAGE_INVALID_TOKEN = "유효하지 않은 로그인 정보입니다.";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        member.validPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(ERROR_MESSAGE_INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = findMemberByEmail(email);
        return new LoginMember(member.getId(), member.emailValue(), member.ageValue());
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(Email.from(email)).orElseThrow(
                () -> new AuthorizationException(Member.ERROR_MESSAGE_VALID_ID_OR_PASSWORD)
        );
    }
}
