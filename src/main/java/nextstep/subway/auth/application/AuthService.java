package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;

@Service
public class AuthService {
	private MemberService memberService;
	private JwtTokenProvider jwtTokenProvider;

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
			throw new AuthorizationException("유효하지 않은 token입니다.");
		}

		String email = jwtTokenProvider.getPayload(credentials);
		Member member = memberService.findByEmail(email);
		return new LoginMember(member.getId(), member.getEmail(), member.getAge());
	}
}
