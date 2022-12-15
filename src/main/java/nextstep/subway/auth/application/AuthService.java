package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.exception.InValidAccessTokenException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.message.AuthMessage;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
                .orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials, boolean required) {
        if (isAnonymous(credentials) && !required) {
            return LoginMember.anonymous();
        }
        validateAccessToken(credentials);
        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(AuthorizationException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private boolean isAnonymous(String credentials) {
        return Objects.isNull(credentials) || "null".equalsIgnoreCase(credentials);
    }

    private void validateAccessToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new InValidAccessTokenException(AuthMessage.AUTH_ERROR_TOKEN_IS_NOT_VALID.message());
        }
    }
}
