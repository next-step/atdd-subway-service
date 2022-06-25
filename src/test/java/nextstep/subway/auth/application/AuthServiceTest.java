package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.infrastructure.TokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
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
    public static final int AGE = 10;

    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberRepository, tokenProvider);
    }

    @Test
    @DisplayName("토큰 로그인")
    void login() {
        유저_조회();
        when(tokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(TokenRequest.of(EMAIL, PASSWORD));
        assertThat(token.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("토큰 정보로 유저 조회")
    void findMemberByToken() {
        // given
        when(tokenProvider.validateToken("TOKEN")).thenReturn(true);
        when(tokenProvider.getPayload("TOKEN")).thenReturn("email@email.com");
        유저_조회();
        // when
        final LoginMember loginMember = authService.findMemberByToken("TOKEN");
        // then
        assertThat(loginMember).isEqualTo(LoginMember.of(null, "email@email.com", 10));
    }

    @Test
    @DisplayName("올바르지 않은 토큰 오류")
    void invalidTokenException() {
        when(tokenProvider.validateToken("TOKEN")).thenReturn(false);
        assertThatThrownBy(() -> authService.findMemberByToken("TOKEN"))
                .isInstanceOf(AuthorizationException.class);
    }

    private void 유저_조회() {
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(Member.of(EMAIL, PASSWORD, AGE)));
    }
}
