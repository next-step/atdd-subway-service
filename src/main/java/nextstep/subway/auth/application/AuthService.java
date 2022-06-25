package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.exception.AuthorizationExceptionType;
import nextstep.subway.auth.infrastructure.TokenProvider;
import nextstep.subway.member.domain.Email;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.exception.MemberExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(final MemberRepository memberRepository, final TokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public TokenResponse login(final TokenRequest request) {
        final Member member = findByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        final String token = tokenProvider.createToken(request.getEmail());
        return TokenResponse.of(token);
    }

    @Transactional(readOnly = true)
    public LoginMember findMemberByToken(final String credentials) {
        if (!tokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(AuthorizationExceptionType.EXPIRE_TOKEN);
        }

        final String email = tokenProvider.getPayload(credentials);
        final Member member = findByEmail(email);
        return LoginMember.of(member.getId(), member.getEmail(), member.getAge());
    }

    private Member findByEmail(final String email) {
        return memberRepository.findByEmail(Email.of(email))
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_EXISTS_EMAIL));
    }
}
