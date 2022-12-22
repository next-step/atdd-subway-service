package nextstep.subway.auth.application;

import nextstep.subway.auth.constants.AuthErrorMessages;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

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

    public LoginMember findMemberByToken(String credentials, boolean compulsoriness) {
        validateTokenIfCompulsory(credentials, compulsoriness);
        if (!isCredentialValid(credentials)) {
            return LoginMember.emptyLoginMember();
        }
        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (isMemberExist(member, compulsoriness)) {
            return LoginMember.emptyLoginMember();
        }
        return LoginMember.from(member);
    }

    private void validateTokenIfCompulsory(String credentials, boolean compulsoriness) {
        if (compulsoriness && !jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(
                    AuthErrorMessages.UNAUTHORIZED_MEMBER_REQUESTED_FAVORITE_CREATION);
        }
    }

    private boolean isCredentialValid(String credentials) {
        return credentials != null && !credentials.isEmpty();
    }

    private boolean isMemberExist(Member member, boolean compulsoriness) {
        return member == null && !compulsoriness;
    }
}
