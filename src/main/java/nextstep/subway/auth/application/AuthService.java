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
	private static final String GUEST_EMAIL = "guest@guest.com";
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
			Member member = memberService.findByEmail(GUEST_EMAIL);
			return new LoginMember(member.getId(), GUEST_EMAIL, 30);
		}

		String email = jwtTokenProvider.getPayload(credentials);
		Member member = memberService.findByEmail(email);
		return new LoginMember(member.getId(), member.getEmail(), member.getAge());
	}
}
