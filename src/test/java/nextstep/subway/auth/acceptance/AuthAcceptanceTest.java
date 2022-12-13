package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 로그인 기능
     *  Scenario: 로그인을 시도한다.
     *      given: 회원이 등록되어 있음.
     *      when: 등록된 이메일로 로그인을 시도하면
     *      then: 로그인에 성공한다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_생성을_요청("byunsw4@naver.com", "password123!", 30);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청("byunsw4@naver.com", "password123!");

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse token = loginResponse.as(TokenResponse.class);
        assertThat(token).isNotNull();
    }

    /**
     * Feature: 로그인 기능
     *  Scenario: 로그인을 시도한다.
     *      given: 회원이 등록되어 있음.
     *      when: 등록되지 않은 이메일로 로그인을 시도하면
     *      then: 로그인에 실패한다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        회원_생성을_요청("byunsw4@naver.com", "password123!", 30);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청("byunsw4@naver2.com", "password123!");

        // then
        로그인_실패됨(loginResponse);
    }


    /**
     * Feature: 유효하지 않는 토큰으로 내 정보 조회 화면을 조회할 수 없다.
     *  Scenario: 내 정보 조회 화면 조회
     *      given:
     *      when: 유효하지 않는 토큰으로 내 정보 조회 를 시도하면
     *      then: 내 정보 조회에 실패한다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given

        // when
        ExtractableResponse<Response> response = 내_정보_조회("test.token.atdd");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    public static ExtractableResponse<Response> 내_정보_조회(String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer "+ token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> loginResponse) {
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
