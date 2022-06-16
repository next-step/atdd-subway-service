package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {
    private ExtractableResponse<Response> 회원_생성_응답_결과;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_응답_결과 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }
    /**
     * Feature: 로그인 기능
     *
     * Scenario: 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_응답_결과 = 로그인_요청(EMAIL, PASSWORD);

        //then
        로그인_성공(로그인_응답_결과);
    }

    /**
     * Feature: 로그인 기능
     *
     * Scenario: 로그인 실패
     * When 등록되지 않은 정보로 로그인 요청
     * Then 로그인 실패됨
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_응답_결과 = 로그인_요청("test@example.com", "1234");

        //then
        권한_없음(로그인_응답_결과);
    }

    /**
     * Feature: 로그인 기능
     *
     * Scenario: 로그인 실패
     * When 유효하지 않은 토큰으로 내 정보 조회 요청
     * Then 조회 실패됨
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        String 유효하지_않은_토큰 = "Invalid Token";
        ExtractableResponse<Response> 내_정보_조회_응답_결과 = 내_정보_조회_요청(유효하지_않은_토큰);

        //then
        권한_없음(내_정보_조회_응답_결과);
    }

    public static String 로그인_되어_있음(String email, String password) {
        TokenResponse tokenResponse = 로그인_요청(email, password).as(TokenResponse.class);
        return tokenResponse.getAccessToken();
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
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    public static void 권한_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
