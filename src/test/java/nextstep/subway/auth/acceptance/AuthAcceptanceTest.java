package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.utils.RestAssuredRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(loginResponse);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 잘못된 로그인 요청
     *     Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, "WRONG_PASSWORD");

        // then
        로그인_실패(loginResponse);
    }

    /**
     * Feature: 회원정보 조회 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 유효한 토큰 사용하여 회원정보 조회
     *     Then 회원정보 조회 성공
     */
    @DisplayName("Bearer Auth 유효한 토큰")
    @Test
    void myInfoWithValidBearerAuth() {
        //Given
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        //When
        ExtractableResponse<Response> response = 내_정보_조회_요청(tokenResponse.getAccessToken());

        //Then
        회원정보_조회_성공(response);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 유효하지 않은 토큰 사용하여 회원정보 조회
     *     Then 회원정보 조회 실패
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청("INVALID_TOKEN");

        // then
        회원정보_조회_실패(findResponse);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return post("/login/token", tokenRequest);
    }

    private static void 로그인_성공(ExtractableResponse<Response> response) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(TokenResponse.class)).isNotNull()
        );
    }

    public static String 로그인_되어_있음(String email, String password) {
        TokenResponse tokenResponse = 로그인_요청(email, password).as(TokenResponse.class);

        return tokenResponse.getAccessToken();
    }

    private static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static void 회원정보_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 회원정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
