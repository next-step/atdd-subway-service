package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.member.application.exception.MemberErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.infrastructure.MemberRepository;
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
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("유효한 토큰이 아닙니다.");
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> NotFoundException.of(MemberErrorCode.MEMBER_NOTFOUND));
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
