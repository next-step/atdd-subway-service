package nextstep.subway.auth.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.step.MemberStep.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthStep {

    public static void 회원_등록되어_있음(String email, String password, Integer age) {
        회원_생성을_요청(email, password, age);
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
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

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static TokenResponse 로그인_되어_있음(String email, String password) {
        return 로그인_요청(email, password).as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
