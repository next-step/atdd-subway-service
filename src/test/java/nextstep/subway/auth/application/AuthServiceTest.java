package nextstep.subway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        given(jwtTokenProvider.createToken(anyString())).willReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("토큰으로 사용자 정보를 찾을 수 있다.")
    @Test
    void findMemberByToken() {
        //given
        String token = "temp_token_string";
        doNothing().when(jwtTokenProvider).validateToken(token);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(EMAIL);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        //when
        LoginMember loginMember = authService.findMemberByToken(token);

        //then
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getAge()).isEqualTo(AGE);
    }

    @DisplayName("잘못된 토큰일 경우 AuthorizationException 에러가 발생한다.")
    @Test
    void authorizationException() {
        //given
        String token = "temp_token_string";

        //then
        Assertions.assertThatThrownBy(() -> authService.findMemberByToken(token))
                .isInstanceOf(AuthorizationException.class);

    }
}
