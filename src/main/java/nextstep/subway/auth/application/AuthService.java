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
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException();
        }

        Long memberId = Long.parseLong(jwtTokenProvider.getPayload(credentials));
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
