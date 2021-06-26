package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
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
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("유효하지 않은 토큰 일때 익명 사용자를 사용할 수 있다.")
    @Test
    void anonymousTest() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        LoginMember member = authService.findMemberByToken("invalid_token", true);

        assertThat(member.isAnonymous()).isTrue();
    }

    @DisplayName("유효하지 않은 토큰 일때 예외가 발생할 수 있다.")
    @Test
    void invalidTokenTest() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);
        assertThatThrownBy(() -> authService.findMemberByToken("invalid_token", false))
        .isInstanceOf(AuthorizationException.class)
        .hasMessageContaining("유효하지 않은 사용자 입니다.");
    }
}
