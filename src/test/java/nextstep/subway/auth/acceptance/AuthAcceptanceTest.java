package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.내_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.utils.RestAssuredUtils.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

@DisplayName("로그인 관련 ")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String LOGIN_BASE_URL = "/login/token";
    public static final String 유효하지_않은_인증_토큰 = "invalid access token";

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Given 회원 가입 되어있음
     * When 로그인을 요청하면
     * Then 토큰을 발급 받는다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // Then
        로그인_됨(response);
    }

    /**
     * Given 유효하지 않은 회원 정보로
     * When 로그인을 요청하면
     * Then 토큰 발급에 실패한다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @ParameterizedTest(name = "case[{index}] : ({0} , {1}) => {2}")
    @MethodSource
    void myInfoWithBadBearerAuth(
        final String email,
        final String password,
        final String testDescription
    ) {
        // When
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // Then
        로그인_실패함(response);
    }

    private static Stream<Arguments> myInfoWithBadBearerAuth() {
        return Stream.of(
            Arguments.of("Not exist email", "Incorrect password", "존재 하지 않는 회원의 로그인 요청"),
            Arguments.of(EMAIL, "Incorrect password", "비밀번호가 일치 하지 않는 로그인 요청")
        );
    }

    /**
     * Given 유효하지 않은 토큰 정보로
     * When 로그인을 요청하면
     * Then 토큰 발급에 실패한다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    void myInfoWithWrongBearerAuth(final String invalidAccessToken, final String testDescription) {
        // When
        ExtractableResponse<Response> response = 내_정보_조회_요청(invalidAccessToken);

        // Then
        인증_실패됨(response);
    }

    private static Stream<Arguments> myInfoWithWrongBearerAuth() {
        return Stream.of(
            Arguments.of("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZXNvdXJjZS1hY2Nlc3MiLCJhdWQiOiJjbGllbnQtc2VydmVyIiwidXNlIjoiQUNDRVNTIiwiaXNzIjoiaW8uZXhhbXBsZS5qd3QiLCJleHAiOjE2NTY2NzY0ODMsImlhdCI6MTY1NjY3NTg4MywiYXV0aG9yaXRpZXMiOiJST0xFX01FTUJFUiIsInVzZXJuYW1lIjoicHJvamVjdC5sb2cuMDYyQGdtYWlsLmNvbSJ9.05gEn1KA0-zikzQhBw9rFMW62r-5m-rzS0i7sH84Yrc", "잘못된 서명을 가진 인증 토큰"),
            Arguments.of("aaa", "JWT 형식이 아닌 인증 토큰"),
            Arguments.of("header.payload.signature", "JSON 형식이 아닌 인증 토큰")
        );
    }

    public static ExtractableResponse<Response> 로그인_요청(
        final String email,
        final String password
    ) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return post(LOGIN_BASE_URL, tokenRequest);
    }

    public static String 토큰_발급_요청(
        final String email,
        final String password
    ) {
        return 로그인_요청(email, password)
            .as(TokenResponse.class)
            .getAccessToken();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    public static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
