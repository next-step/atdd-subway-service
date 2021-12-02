package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.exception.NotRegistedMemberException;
import nextstep.subway.common.exception.UnauthoriedRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

import java.util.Optional;

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

    public LoginMember findMemberByToken(String credentials) {
        if (credentials == null || credentials.equals("null")) {
            return new LoginMember();
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new UnauthoriedRequestException("인증되지 않은 사용자로 요청되었습니다.");
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Optional<Member> member = memberRepository.findByEmail(email);

        if (!member.isPresent()) {
            throw new NotRegistedMemberException("등록되지 않은 사용자입니다.");
        }

        return new LoginMember(member.get().getId(), member.get().getEmail(), member.get().getAge());
    }
}
