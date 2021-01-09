package nextstep.subway.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.MemberNotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
