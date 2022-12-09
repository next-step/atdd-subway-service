package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String WRONG_PASSWORD = "wrong_password";

    /**
     * Feature: 로그인 성공
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     */
    @DisplayName("로그인 성공 관련")
    @TestFactory
    Stream<DynamicTest> loginSuccess() {
        return Stream.of(
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("등록한 회원으로 로그인에 성공하고 토큰을 발급받는다", 로그인_요청_성공(EMAIL, PASSWORD))
        );
    }

    /**
     * Feature: 로그인 실패
     *   Scenario: 로그인을 시도한다.
     *     Given 회원이 등록되어 있지 않음
     *     When 로그인 요청
     *     Then 로그인 실패
     */
    @DisplayName("로그인 실패 관련")
    @TestFactory
    Stream<DynamicTest> loginFail() {
        return Stream.of(
                dynamicTest("등록한 회원이 없는 경우 예외가 발생한다", 로그인_요청_실패(EMAIL, PASSWORD)),
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("패스워드가 일치하지 않아 예외가 발생한다", 로그인_요청_실패(EMAIL, WRONG_PASSWORD))
        );
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public Executable 로그인_요청_성공(String email, String password) {
        return () -> {
            ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(email, password));
            로그인_성공하고_토큰을_발급받는다(response);
        };
    }

    public Executable 로그인_요청_실패(String email, String password) {
        return () -> {
            ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(email, password));
            로그인_실패한다(response);
        };
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공하고_토큰을_발급받는다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    public static void 로그인_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.jsonPath().getString("accessToken")).isNull();
    }
}
