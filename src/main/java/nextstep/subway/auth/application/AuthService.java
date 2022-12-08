package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

import static nextstep.subway.common.domain.BizExceptionMessages.AUTHORIZATION_WRONG_ACCESS_TOKEN;

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

    public LoginMember findMemberByToken(String credentials, boolean isRequired) {
        boolean isValidToken = jwtTokenProvider.validateToken(credentials);

        if (isRequired && !isValidToken) {
            throw new AuthorizationException(AUTHORIZATION_WRONG_ACCESS_TOKEN.message());
        }
        if (!isRequired && !isValidToken) {
            return LoginMember.guest();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(NoResultException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
