package nextstep.subway.auth.application;

import static nextstep.subway.exception.ExceptionMessage.*;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthorizationException(WRONG_AUTH));
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials, boolean loginRequired) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return findGuestMemberOrThrows(loginRequired);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_DATA));
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private LoginMember findGuestMemberOrThrows(boolean loginRequired) {
        if (loginRequired) {
            throw new AuthorizationException(WRONG_TOKEN);
        }
        return new LoginMember();
    }
}
