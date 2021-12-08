package nextstep.subway.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(AuthorizationException::new);

        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("토큰이 유효하지 않습니다.");
        }

        String id = jwtTokenProvider.getPayload(credentials);

        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(AuthorizationException::new);

        return LoginMember.of(member);
    }
}
