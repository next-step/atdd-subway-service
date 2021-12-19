package nextstep.subway.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 10;

	private AuthService authService;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		authService = new AuthService(memberRepository, jwtTokenProvider);
	}

	@Test
	void login() {
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(Member.of(EMAIL, PASSWORD, AGE)));
		when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

		TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

		assertThat(token.getAccessToken()).isNotBlank();
	}
}
