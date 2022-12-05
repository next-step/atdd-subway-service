package nextstep.subway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    void 등록된_이메일과_비밀번호와_유효한_토큰으로_로그인() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @Test
    void 등록되지_않은_이메일_로그인_요청시_실패() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        TokenRequest token = new TokenRequest(EMAIL, PASSWORD);
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_EMAIL.message());
    }

    @Test
    void 등록되지_않은_비밀번호로_로그인_요청시_실패() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        TokenRequest token = new TokenRequest(EMAIL, "wrong password");
        Assertions.assertThatThrownBy(() -> authService.login(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_MATCH_PASSWORD.message());
    }

    @Test
    void 유효하지_않은_토큰으로_로그인_요청시_실패() {
        Assertions.assertThatThrownBy(() -> authService.findMemberByToken("wrong access token"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ErrorEnum.INVALID_TOKEN.message());
    }

    @Test
    void 유효한_토큰으로_로그인_요청시_회원정보_반환() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        LoginMember member = authService.findMemberByToken("TOKEN");

        Assertions.assertThat(member).isEqualTo(new LoginMember(member.getId(), EMAIL, AGE));
    }
}
