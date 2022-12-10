package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.auth.domain.LoginMember.GUEST_DEFAULT_EMAIL;
import static nextstep.subway.auth.domain.LoginMember.GUEST_DEFAULT_ID;
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
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberRepository, jwtTokenProvider);
    }

    @Test
    void login() {
        when(memberRepository.findByEmail(anyString())).thenReturn(
                Optional.of(new Member(EMAIL, PASSWORD, Age.from(AGE))));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("TOKEN");

        TokenResponse token = authService.login(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("guest 멤버 테스트")
    @Test
    void findByMemberToken_guestUser_success() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);
        LoginMember guest = authService.findMemberByToken("", false);
        assertThat(guest.getId()).isEqualTo(GUEST_DEFAULT_ID);
        assertThat(guest.getEmail()).isEqualTo(GUEST_DEFAULT_EMAIL);
    }

    @DisplayName("인증이 필요한 api에서 인증 정보가 일치하지 않는 경우")
    @Test
    void findByMemberToken_loginMember_AuthorizationException() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);
        assertThatThrownBy(() -> authService.findMemberByToken("", true)).isInstanceOf(AuthorizationException.class);
    }
}
