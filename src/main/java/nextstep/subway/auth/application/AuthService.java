package nextstep.subway.auth.application;

import java.util.Objects;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.exception.DataNotExistException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private static final String INVALID_TOKEN_ERROR_MESSAGE = "유효한 Token 정보가 아닙니다.";

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
        return TokenResponse.from(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (Objects.isNull(credentials)) {
            return LoginMember.createEmpty();
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new IllegalArgumentException(INVALID_TOKEN_ERROR_MESSAGE);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(DataNotExistException::new);
        return LoginMember.of(member.getId(), member.getEmail(), member.getAge());
    }
}
