package nextstep.subway.auth.application;

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
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
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

    @DisplayName("토큰으로 멤버 조회")
    @Test
    void findMemberByToken() {
        // given
        String credentials = "credentials";
        given(jwtTokenProvider.validateToken(credentials)).willReturn(true);
        given(jwtTokenProvider.getPayload(credentials)).willReturn(EMAIL);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        // when
        LoginMember loginMember = authService.findMemberByToken(credentials);

        // then
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getAge()).isEqualTo(AGE);
    }

    @DisplayName("토큰으로 멤버 조회 실패 - 잘못된 토큰")
    @Test
    void findMemberByToken_invalidToken() {
        // given
        String credentials = "credentials";
        given(jwtTokenProvider.validateToken(credentials)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> authService.findMemberByToken(credentials))
            .isInstanceOf(AuthorizationException.class);
    }
}
