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
        final Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        final String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(final String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException();
        }

        final String email = jwtTokenProvider.getPayload(credentials);
        final Member member = memberRepository.findByEmail(email)
            .orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
