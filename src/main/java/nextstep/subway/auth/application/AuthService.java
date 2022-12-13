package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.exception.AuthorizationException;
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
		Member member = memberRepository.findByEmail(request.email())
			.orElseThrow(AuthorizationException::new);
		member.checkPassword(request.password());

		String token = jwtTokenProvider.createToken(request.getEmail());
		return new TokenResponse(token);
	}

	public LoginMember findMemberByToken(String credentials) {
		if (!jwtTokenProvider.validateToken(credentials)) {
			throw new AuthorizationException("유효하지 않은 토큰입니다.");
		}

		Email email = Email.from(jwtTokenProvider.getPayload(credentials));
		Member member = memberRepository.findByEmail(email).orElseThrow(
			() -> new AuthorizationException(String.format("해당 이메일(%s) 이 존재하지 않습니다.", email))
		);
		return LoginMember.of(member.getId(), member.email(), member.age());
	}
}
