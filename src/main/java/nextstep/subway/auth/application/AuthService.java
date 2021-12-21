package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public TokenResponse login(TokenRequest request) {
		Member member = memberService.findByEmail(request.getEmail());
		member.checkPassword(request.getPassword());

		String token = jwtTokenProvider.createToken(request.getEmail());
		return new TokenResponse(token);
	}

	public LoginMember findMemberByToken(String credentials) {
		if (!jwtTokenProvider.validateToken(credentials)) {
			throw new AuthorizationException("다시 로그인 해주시기 바랍니다.");
		}

		String email = jwtTokenProvider.getPayload(credentials);
		Member member = memberService.findByEmail(email);
		return new LoginMember(member);
	}
}
