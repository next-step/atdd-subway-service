package nextstep.subway.auth.application;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String WRONG_EMAIL = "wrong@email.com";
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
    
    @DisplayName("회원가입한 이메일과 다른 이메일로 로그인을 시도하면 AuthorizationException 발생")
    @Test
    void login_fail_wrong_email() {
        // given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");
        when(authService.login(new TokenRequest(WRONG_EMAIL, PASSWORD))).thenThrow(new AuthorizationException());
        // then
        assertThatThrownBy(() -> authService.login(new TokenRequest(WRONG_EMAIL, PASSWORD))).isInstanceOf(AuthorizationException.class);
    }
}
