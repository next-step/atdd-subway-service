package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
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
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("등록된 회원정보와 유효한 토큰으로 로그인을 한다.")
    @Test
    void login() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("등록되지 않은 회원정보로 로그인 시도 시 예외가 발생한다.")
    @Test
    void loginExceptionNotExists() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        TokenRequest token = new TokenRequest(EMAIL, PASSWORD);
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(MEMBER_NOT_EXISTS);
    }

    @DisplayName("토큰이 유효하지 않으면 나이가 20인 LoginMember 객체를 리턴한다.")
    @Test
    void findMemberByInvalidAccessTokenException() {
        LoginMember loginMember = authService.findMemberByToken("invalid access token");

        Assertions.assertThat(loginMember.getAge().equals(20));
    }

    @DisplayName("잘못된 비밀번호로 로그인 시도 시 예외가 발생한다.")
    @Test
    void loginExceptionByWrongPassword() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        TokenRequest token = new TokenRequest(EMAIL, "wrong password");
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(WRONG_PASSWORD);
    }

    @DisplayName("토큰이 유효할 경우 회원정보를 반환한다.")
    @Test
    void findMemberByToken() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        LoginMember member = authService.findMemberByToken("token");

        Assertions.assertThat(member).isEqualTo(new LoginMember(member.getId(), EMAIL, AGE));
    }
}
