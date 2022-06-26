package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static nextstep.subway.common.Messages.INVALID_TOKEN;
import static nextstep.subway.common.Messages.REQUIRED_MEMBER;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

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

    public LoginMember findMemberByToken(boolean required, String credentials) {
        if (required && StringUtils.isEmpty(credentials)) {
            throw new AuthorizationException(REQUIRED_MEMBER);
        }

        if (StringUtils.isEmpty(credentials)) {
            return LoginMember.ofGuestMember();
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
