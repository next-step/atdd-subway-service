package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.constants.ErrorMessages;
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
        if (compulsoriness && !jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(
                    ErrorMessages.UNAUTHORIZED_MEMBER_REQUESTED_FAVORITE_CREATION);
        }
        if (credentials == null && !compulsoriness) {
            return LoginMember.emptyLoginMember();
        }
        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email)
                .orElse(null);
        if (member == null && !compulsoriness) {
            return LoginMember.emptyLoginMember();
        }
        return LoginMember.from(member);
    }
}
