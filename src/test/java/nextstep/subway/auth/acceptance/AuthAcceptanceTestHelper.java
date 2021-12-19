package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;

public class AuthAcceptanceTestHelper {
    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest token = new TokenRequest(email, password);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(token)
            .when().post("login/token")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all().extract();
    }

    public static String 토큰_정보(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String token = response.as(TokenResponse.class).getAccessToken();
        assertThat(token).isNotNull();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 내_정보_조회_됨(ExtractableResponse<Response> response, String email, String age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }
}
