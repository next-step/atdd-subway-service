package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;


public class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * Given : 회원 등록이 되어 있음
     * When : 로그인 요청
     * Then : 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // Given
        final String PASSWORD = "password";
        final String EMAIL = "email@email.com";
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, 20);
        회원_생성됨(createResponse);

        // When
        ExtractableResponse<Response> response = 로그인_요청(암호_이메일_입력(PASSWORD, EMAIL));

        // Then
        로그인_됨(response);
    }

    /**
     * Given : 비회원 사용자가
     * When : 로그인 시도 시
     * Then : 로그인이 실패 된다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // Given
        final String INVALID_PASSWORD = "badPassword";
        final String INVALID_EMAIL = "bad@email.com";

        //when
        ExtractableResponse<Response> response = 로그인_요청(암호_이메일_입력(INVALID_PASSWORD, INVALID_EMAIL));

        //Then
        인증_실패됨(response);
    }

    /**
     * Given : 등록된 사용자 이지만
     * When: 정보가 일치하지 않으면
     * Then: 로그인이 실패 된다.
     */
    @DisplayName("등록된 사용자 이지만 정보가 일치 하지 않으면 로그인이 실패")
    @Test
    void invalidUserInfoWithBearerAuth() {
        // Given
        final String PASSWORD = "password";
        final String EMAIL = "email@email.com";
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, 20);
        회원_생성됨(createResponse);

        // When
        ExtractableResponse<Response> response = 로그인_요청(암호_이메일_입력("noMatch", EMAIL));

        //Then
        인증_실패됨(response);
    }

    /**
     * When 유효하지 않은 토큰으로 사용자 정보 요청 시
     * Then 인증이 실패 된다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        // When
        ExtractableResponse<Response> response = 변조된_인증_정보로_사용자_정보_요청("abcdefg");

        // Then
        인증_실패됨(response);
    }

    public static void 인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.valueOf(response.statusCode())).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.valueOf(response.statusCode())).isEqualTo(HttpStatus.OK);
        String accessToken = response.jsonPath().get("accessToken");
        assertThat(accessToken).isNotBlank();
    }

    public static ExtractableResponse<Response> 로그인_요청(Map<String, String> body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(body)
                .post("/login/token")
                .then()
                .log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 변조된_인증_정보로_사용자_정보_요청(final String badToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(badToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static Map<String, String> 암호_이메일_입력(final String password, final String email) {
        return new HashMap<String, String>() {{
            put("password", password);
            put("email", email);
        }};
    }
}
