package nextstep.subway.auth.application;

import nextstep.subway.auth.application.exception.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.MemberQueryService;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    public static final String FAILED_VALIDATION_TOKEN_EXCEPTION_MESSAGE = "토큰 검증에 실패하였습니다.";

    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberQueryService memberQueryService, JwtTokenProvider jwtTokenProvider) {
        this.memberQueryService = memberQueryService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberQueryService.findMemberByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(FAILED_VALIDATION_TOKEN_EXCEPTION_MESSAGE);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberQueryService.findMemberByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
