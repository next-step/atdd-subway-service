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
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException("아이디 또는 비밀번호를 다시 확인해주세요."));

        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(member.getId().toString());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        validateToken(credentials);

        Long memberId = Long.parseLong(jwtTokenProvider.getPayload(credentials));
        Member member = findById(memberId);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원이니다."));
    }

    private void validateToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("유효하지 않는 토큰입니다.");
        }
    }
}
