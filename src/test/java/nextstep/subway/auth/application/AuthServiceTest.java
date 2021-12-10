package nextstep.subway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("인증 관련 서비스 테스트")
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
    @DisplayName("로그인")
    void login() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("token으로 사용자 찾기")
    void findMemberByToken() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn("1");
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        LoginMember loginMember = authService.findMemberByToken("TOKEN");

        assertAll(() -> {
            assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
            assertThat(loginMember.getAge()).isEqualTo(AGE);
        });
    }

    @Test
    @DisplayName("token으로 사용자 찾기 실패")
    void findMemberByTokenFail() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.findMemberByToken("invalidToken"))
            .isInstanceOf(AuthorizationException.class);
    }
}
