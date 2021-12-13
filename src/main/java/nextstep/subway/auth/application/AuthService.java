package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
	private MemberRepository memberRepository;
	private JwtTokenProvider jwtTokenProvider;

	private static final String ERORR_MESSAGE_ILLEGAL_ACCESS_TOKEN="인증 정보가 올바르지 않습니다.";

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
		if (!jwtTokenProvider.validateToken(credentials)) {
			throw new AuthorizationException(ERORR_MESSAGE_ILLEGAL_ACCESS_TOKEN);
		}

		String email = jwtTokenProvider.getPayload(credentials);
		Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
		return new LoginMember(member.getId(), member.getEmail(), member.getAge());
	}
}