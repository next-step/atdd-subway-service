package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        //given 회원 등록
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /*
    Feature: 로그인 기능
    Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        //then 로그인 성공
        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "wrong password");

        //then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = 본인_정보_조회_요청("wrong token");

        //then
        토큰_인증_실패(response);
    }


    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 토큰_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    public static void 토큰_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
