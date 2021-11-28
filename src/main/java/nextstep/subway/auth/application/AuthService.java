package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.domain.Email;
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
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new AuthorizationException(
                String.format("%s 대한 정보가 존재하지 않습니다.", request.email())));
        member.checkPassword(request.password());

        String token = jwtTokenProvider.createToken(request.email().toString());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(String.format("유효하지 않은 토큰(%s)입니다.", credentials));
        }

        Email email = Email.from(jwtTokenProvider.getPayload(credentials));
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new AuthorizationException(String.format("이메일(%s)이 존재하지 않습니다.", email)));
        return LoginMember.of(member.id(), member.email(), member.age());
    }
}
