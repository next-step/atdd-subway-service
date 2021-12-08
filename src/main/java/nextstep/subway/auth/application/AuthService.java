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
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException("요청한 이메일을 사용하는 사용자가 존재하지 않습니다."));
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("전달받은 토큰이 잘못되었습니다.");
        }

        Long memberId = Long.parseLong(jwtTokenProvider.getPayload(credentials));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthorizationException("토큰과 일치하는 사용자가 존재하지 않습니다."));

        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
