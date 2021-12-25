package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@Service
public class AuthService {
	private MemberRepository memberRepository;
	private JwtTokenProvider jwtTokenProvider;

	public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public TokenResponse login(TokenRequest request) {
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "잘못된 토큰 정보입니다"));
		member.checkPassword(request.getPassword());

		String token = jwtTokenProvider.createToken(request.getEmail());
		return new TokenResponse(token);
	}

	public LoginMember findMemberByToken(String credentials, boolean required) {
		if (!jwtTokenProvider.validateToken(credentials)) {
			return ifNotRequiredEmptyMember(required, credentials);
		}

		String email = jwtTokenProvider.getPayload(credentials);
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "잘못된 토큰 정보입니다"));
		return member.toLoginMember();
	}

	private LoginMember ifNotRequiredEmptyMember(boolean required, String credentials) {
		if (!required) {
			return new LoginMember();
		}
		throw new AppException(ErrorCode.UNAUTHORIZED, "인증에 실패했습니다. credential: {}", credentials);

	}
}
