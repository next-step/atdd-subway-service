package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
        final MemberRepository memberRepository,
        final JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(final TokenRequest request) {
        final Member member = findMemberByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        final String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(final String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return LoginMember.GUEST;
        }

        final String email = jwtTokenProvider.getPayload(credentials);
        final Member member = findMemberByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(AuthorizationException::new);
    }
}
