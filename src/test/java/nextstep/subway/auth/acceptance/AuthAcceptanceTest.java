package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Given: 회원 등록되어 있음
     * When: 토큰 발급(로그인) 요청
     * Then: 토큰 발급(로그인) 성공
     * When: 내 정보 조회 요청
     * Then: 내 정보 조회 성공
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> 토큰_발급_응답 = 토큰_발급_요청(EMAIL, PASSWORD);
        // then
        토큰_발급_성공(토큰_발급_응답);
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(토큰값_추출(토큰_발급_응답));
        // then
        내_정보_조회_성공(내_정보_조회_응답, EMAIL, AGE);
    }

    /**
     * Given: 회원 등록되어 있음
     * When: 잘못된 이메일로 토큰 발급(로그인) 요청
     * Then: 토큰 발급(로그인) 실패
     * When: 잘못된 패스워드로 토큰 발급(로그인) 요청
     * Then: 토큰 발급(로그인) 실패
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String WRONG_EMAIL = "WRONG_EMAIL";
        String WRONG_PASSWORD = "WRONG_PASSWORD";

        // when
        ExtractableResponse<Response> 잘못된_EMAIL_토큰_발급_응답 = 토큰_발급_요청(WRONG_EMAIL, PASSWORD);
        // then
        토큰_발급_실패(잘못된_EMAIL_토큰_발급_응답);
        // when
        ExtractableResponse<Response> 잘못된_PASSWORD_토큰_발급_응답 = 토큰_발급_요청(EMAIL, WRONG_PASSWORD);
        // then
        토큰_발급_실패(잘못된_PASSWORD_토큰_발급_응답);
    }

    /**
     * Given: 회원 등록되어 있음
     * When: 토큰 발급(로그인) 요청
     * Then: 토큰 발급(로그인) 성공
     * When: 잘못된 토큰으로 내 정보 조회 요청
     * Then: 내 정보 조회 실패
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String WRONG_TOKEN = "WRONG_TOKEN";
        // when
        ExtractableResponse<Response> 토큰_발급_응답 = 토큰_발급_요청(EMAIL, PASSWORD);
        // then
        토큰_발급_성공(토큰_발급_응답);
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(WRONG_TOKEN);
        // then
        내_정보_조회_실패(내_정보_조회_응답);
    }

    public static ExtractableResponse<Response> 토큰_발급_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static void 토큰_발급_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    public static String 토큰값_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    private void 토큰_발급_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
