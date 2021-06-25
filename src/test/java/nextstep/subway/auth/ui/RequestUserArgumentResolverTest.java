package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
@ExtendWith(MockitoExtension.class)
class RequestUserArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    @Mock
    MemberRepository memberRepository;

    private AuthService authService;

    private static MockedStatic<AuthorizationExtractor> mockedSettings;

    @BeforeEach
    void setUp() {
        mockedSettings = mockStatic(AuthorizationExtractor.class);
    }

    @AfterEach
    void clear() {
        mockedSettings.close();
    }

    @DisplayName("로그인 상태로 호출시 고객정보가 셋팅된 LoginMember를 가져오자")
    @Test
    void loginMember() {
        // given
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        authService = new AuthService(memberRepository, jwtTokenProvider);

        when(AuthorizationExtractor.extract(request.getNativeRequest(HttpServletRequest.class))).thenReturn("credentials");

        lenient().when(jwtTokenProvider.validateToken("credentials")).thenReturn(true);
        lenient().when(jwtTokenProvider.getPayload("credentials")).thenReturn("test@email.com");
        lenient().when(memberRepository.findByEmail("test@email.com")).thenReturn(Optional.of(new Member("test@email.com", "password", 30)));

        authService = new AuthService(memberRepository, jwtTokenProvider);
        RequestUserArgumentResolver requestUserArgumentResolver = new RequestUserArgumentResolver(authService);

        // when
        Object object = requestUserArgumentResolver.resolveArgument(parameter, null, request, null);

        // then
        assertThat(object).isInstanceOf(LoginMember.class);
        LoginMember loginMember = (LoginMember) object;
        assertThat(loginMember.getEmail()).isEqualTo("test@email.com");
        assertThat(loginMember.getAge()).isEqualTo(30);
    }

    @DisplayName("비로그인 상태로 호출시 반환되는 LoginMember는 빈객체로 온다")
    @Test
    void guestMember() {
        // given
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        authService = new AuthService(memberRepository, jwtTokenProvider);

        when(AuthorizationExtractor.extract(request.getNativeRequest(HttpServletRequest.class))).thenReturn("credentials");
        lenient().when(jwtTokenProvider.validateToken("credentials")).thenReturn(false);

        RequestUserArgumentResolver requestUserArgumentResolver = new RequestUserArgumentResolver(authService);

        //when
        Object object = requestUserArgumentResolver.resolveArgument(parameter, null, request, null);

        // then
        assertThat(object).isInstanceOf(LoginMember.class);
        LoginMember loginMember = (LoginMember) object;
        assertThat(loginMember.getId()).isNull();
        assertThat(loginMember.getEmail()).isNull();
        assertThat(loginMember.getAge()).isNull();
    }
}
