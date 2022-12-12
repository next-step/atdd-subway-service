package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.member.domain.Email;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        validateAccessToken(credentials);

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = findMemberByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private void validateAccessToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(ErrorMessage.UNAUTHORIZED.getMessage());
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(Email.from(email))
                .orElseThrow(() -> new AuthorizationException(ErrorMessage.UNAUTHORIZED.getMessage()));
    }
}
