package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import static nextstep.subway.utils.Message.INVALID_ACCESS_TOKEN;
import static nextstep.subway.utils.Message.MEMBER_NOT_EXISTS;

@Service
public class AuthService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_EXISTS));
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            // 비로그인 시 성인요금으로 간주
            return new LoginMember(20);
//            throw new AuthorizationException(INVALID_ACCESS_TOKEN);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_EXISTS));
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
