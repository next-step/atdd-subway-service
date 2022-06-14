package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 성공
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_성공(로그인_응답);
    }

    /**
     * Given 등록되지 않은 계정
     * When 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String email = "tester@gmail.com";
        String password = "1234";

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(email, password);

        // then
        로그인_실패(로그인_응답);
    }

    /**
     * Given 로그인 하지 않음
     * When 내 정보 조회
     * Then 조회 실패
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        RequestSpecification 비로그인 = 로그인_하지_않음();

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(비로그인);

        // then
        권한_없음(내_정보_조회_응답);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotEmpty();
        return accessToken;
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static RequestSpecification 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        String accessToken = 로그인_성공(response);
        return RestAssured.given().log().all().auth().oauth2(accessToken);
    }

    public static RequestSpecification 로그인_하지_않음() {
        return RestAssured.given().log().all();
    }

    public static void 권한_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
