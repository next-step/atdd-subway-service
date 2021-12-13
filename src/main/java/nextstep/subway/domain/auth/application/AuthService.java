package nextstep.subway.domain.auth.application;

import nextstep.subway.domain.auth.InvalidTokenException;
import nextstep.subway.domain.auth.domain.LoginMember;
import nextstep.subway.domain.auth.dto.TokenRequest;
import nextstep.subway.domain.auth.dto.TokenResponse;
import nextstep.subway.domain.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.domain.member.domain.Member;
import nextstep.subway.domain.member.domain.MemberRepository;
import nextstep.subway.global.exception.EntityNotFoundException;
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

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new InvalidTokenException();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
