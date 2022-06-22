package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.infrastructure.InMemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.infrastructure.JwtTokenProviderTest.EXPIRE_LENGTH;
import static nextstep.subway.auth.infrastructure.JwtTokenProviderTest.SECRET_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthServiceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXPIRE_LENGTH);

        authService = new AuthService(memberRepository, jwtTokenProvider);
    }

    @Test
    void 로그인() {
        // when
        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }

    @Test
    void 토큰으로_멤버_조회하기() {
        // given
        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        // when
        LoginMember result = authService.findMemberByToken(token.getAccessToken());

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(result.getAge()).isEqualTo(AGE)
        );
    }

    @Test
    void 유효하지_않은_토큰으로_멤버를_조회하면_예외가_발생한다() {
        // given
        String token = "invalid";

        // when & then
        assertThatThrownBy(() ->
                authService.findMemberByToken(token)
        ).isInstanceOf(AuthorizationException.class)
                .hasMessage("토큰 값이 일치하지 않습니다.");
    }
}
