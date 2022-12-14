package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.NotFoundDataException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.exception.type.NotFoundDataExceptionType.NOT_FOUND_MEMBER;

@Service
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

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public LoginMember findMemberByToken(String credentials, boolean isRequired) {
        boolean isValidToken = isValidToken(credentials);

        isTokenRequired(isRequired, isValidToken);
        if (!isValidToken) {
            return LoginMember.ofNotLogin();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundDataException(NOT_FOUND_MEMBER.getMessage()));
        return LoginMember.ofLogin(member.getId(), member.getEmail(), member.getAge());
    }

    private boolean isValidToken(String credentials) {
        return jwtTokenProvider.validateToken(credentials);
    }

    private void isTokenRequired(boolean isRequired, boolean isValidToken) {
        if (!isValidToken && isRequired) {
            throw new AuthorizationException();
        }
    }
}
