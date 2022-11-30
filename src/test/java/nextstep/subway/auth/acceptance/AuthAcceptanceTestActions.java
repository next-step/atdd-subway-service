package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTestActions {

    public static void 잘못된_토큰_요청_응답(ExtractableResponse<Response> accessTokenResponse, int statusCode) {
        assertThat(accessTokenResponse.statusCode()).isEqualTo(statusCode);
    }

    public static void 정상적인_토큰_요청_응답(ExtractableResponse<Response> accessTokenResponse, int statusCode, String email,
                                     Integer age) {
        JsonPath jsonPath = accessTokenResponse.jsonPath();
        assertAll(
                () -> assertThat(accessTokenResponse.statusCode()).isEqualTo(statusCode),
                () -> assertThat(jsonPath.getString("id")).isNotBlank(),
                () -> assertThat(jsonPath.getString("email")).isEqualTo(email),
                () -> assertThat(jsonPath.getInt("age")).isEqualTo(age)
        );
    }

    public static ExtractableResponse<Response> 토큰과_함께_요청_시도(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원등록_되어있음(String email, String password, int age) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(email, password, age);
        회원_생성됨(createResponse);
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank()
        );
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
