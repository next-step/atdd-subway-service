package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 *   Feature: 로그인 기능
 *
 *     Background
 *       Given 회원 등록되어 있음
 *
 *     Scenario: 로그인을 시도한다.
 *       When 로그인 요청
 *       Then 로그인 됨
 *
 *     Scenario: 잘못된 비밀번호로 로그인을 시도한다.
 *       When 로그인 요청
 *       Then 로그인 실패
 *
 *     Scenario: 등록하지 않은 회원정보로 로그인을 시도한다.
 *       When 로그인 요청
 *       Then 로그인 실패
 */
class AuthAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Scenario: 로그인을 시도한다.
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));

        로그인_됨(response);
    }

    /**
     * Scenario: 잘못된 비밀번호로 로그인을 시도한다.
     * When 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패(잘못된 비밀번호로 로그인 시도)")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, "wrong password"));

        로그인_안됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank()
        );
    }

    private void 로그인_안됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
