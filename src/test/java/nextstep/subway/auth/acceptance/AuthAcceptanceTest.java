package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String WRONG_PASSWORD = "wrong_password";

    /**
     * Feature: 로그인 관련
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 회원이 존재하지 않은 경우
     *     Then 로그인 실패
     *     When 로그인 요청
     *     Then 토큰 발급 성공
     *     When 잘못된 패스워드로 로그인 요청
     *     Then 로그인 실패
     */
    @DisplayName("로그인 관련")
    @TestFactory
    Stream<DynamicTest> loginSuccess() {
        return Stream.of(
                dynamicTest("등록한 회원이 없는 경우 예외가 발생한다", 로그인_요청_실패(EMAIL, PASSWORD)),
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("등록한 회원으로 로그인에 성공하고 토큰을 발급받는다", 로그인_요청_성공(EMAIL, PASSWORD)),
                dynamicTest("패스워드가 일치하지 않아 예외가 발생한다", 로그인_요청_실패(EMAIL, WRONG_PASSWORD))
        );
    }

    /**
     * Feature: Auth 관련
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 토큰 발급 성공
     *     When 내 정보 조회 요청
     *     Then 내 정보 조회 성공
     *     When 로그인 실패, 내 정보 조회 요청
     *     Then 로그인 실패
     *     When 유효하지 않은 토큰으로 내 정보 조회 요청
     *     Then 내 정보 조회 실패
     */
    @DisplayName("Bearer Auth 관련")
    @TestFactory
    Stream<DynamicTest> myInfoWithBadBearerAuth() {
        return Stream.of(
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("등록한 회원으로 로그인에 성공하고 토큰을 발급받는다", 로그인_요청_성공(EMAIL, PASSWORD)),
                dynamicTest("로그인에 성공하고 내 정보를 조회한다", 내_정보_조회_요청_성공(EMAIL, PASSWORD)),
                dynamicTest("로그인하지 않으면 내 정보를 조회할 수 없다", 로그인_요청_실패(EMAIL, WRONG_PASSWORD)),
                dynamicTest("유효하지 않은 토큰으로 내 정보를 조회할 수 없다", 내_정보_조회_요청_실패("wrongToken"))
        );
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
