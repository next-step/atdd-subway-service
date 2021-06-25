package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@DisplayName("MockHttpServletRequest을 사용하여 RequestUserArgumentResolver테스트")
@ExtendWith(MockitoExtension.class)
class RequestUserArgumentResolverWithUseMockRequestTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    MemberRepository memberRepository;

    private AuthService authService;


    @DisplayName("로그인 상태로 호출시 고객정보가 셋팅된 LoginMember를 가져오자")
    @Test
    void loginMember() {
        // given
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTYyNDYzMzg3NCwiZXhwIjoxNjI0NjM3NDc0fQ.DFZSxcboJHhtB7S4SNDWBJApa7ZlRSd-C9UXjPfsVh8";
        authService = new AuthService(memberRepository, jwtTokenProvider);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer "+authToken);
        ServletWebRequest servletWebRequest = new ServletWebRequest(mockRequest);

        lenient().when(jwtTokenProvider.validateToken(authToken)).thenReturn(true);
        lenient().when(jwtTokenProvider.getPayload(authToken)).thenReturn("test@email.com");
        lenient().when(memberRepository.findByEmail("test@email.com")).thenReturn(Optional.of(new Member("test@email.com", "password", 30)));

        authService = new AuthService(memberRepository, jwtTokenProvider);
        RequestUserArgumentResolver requestUserArgumentResolver = new RequestUserArgumentResolver(authService);

        // when
        Object object = requestUserArgumentResolver.resolveArgument(parameter, null, servletWebRequest, null);

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

        ServletWebRequest servletWebRequest = new ServletWebRequest(new MockHttpServletRequest());
        lenient().when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);
        RequestUserArgumentResolver requestUserArgumentResolver = new RequestUserArgumentResolver(authService);

        //when
        Object object = requestUserArgumentResolver.resolveArgument(parameter, null, servletWebRequest, null);

        // then
        assertThat(object).isInstanceOf(LoginMember.class);
        LoginMember loginMember = (LoginMember) object;
        assertThat(loginMember.getId()).isNull();
        assertThat(loginMember.getEmail()).isNull();
        assertThat(loginMember.getAge()).isNull();
    }
}
