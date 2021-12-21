package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
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
    private MemberService memberService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberService, jwtTokenProvider);
    }

    @Test
    void login() {
        when(memberService.findByEmail(anyString())).thenReturn(new Member(EMAIL, PASSWORD, AGE));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("토큰으로 로그인 여부 확인(로그인 상태) 테스트")
    public void findMemberByTokenTest() {
    	//given
        when(jwtTokenProvider.validateToken("token")).thenReturn(true);
        when(jwtTokenProvider.getPayload("token")).thenReturn(EMAIL);
        when(memberService.findByEmail(anyString())).thenReturn(new Member(1L, EMAIL, PASSWORD, AGE));

        //when
        LoginMember loginMember = authService.findMemberByToken("token");

        //then
        assertThat(loginMember).isEqualTo(new LoginMember(1L, EMAIL, AGE));
    }

}
