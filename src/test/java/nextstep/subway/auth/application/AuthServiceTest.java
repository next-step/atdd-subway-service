package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

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

    @DisplayName("아이디와 패스워드가 정상적으로 주어지면 등록 된 회원인지 검증 후 발급 된 토근을 반환한다")
    @Test
    void login() {
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        given(jwtTokenProvider.createToken(anyString())).willReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("토큰이 주어지면 토큰의 payload에 담긴 이메일 파싱 후 일치되는 회원 정보를 반환한다")
    @Test
    void find_member_by_token() {
        // given
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getPayload(any())).willReturn(EMAIL);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        // when
        LoginMember loginMember = authService.findMemberByToken("TOKEN", true);

        // then
        assertAll(
                () -> assertThat(loginMember.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(loginMember.getAge()).isEqualTo(AGE)
        );
    }
}
