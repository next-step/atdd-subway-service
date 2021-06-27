package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceStep {
    public static final String LOGIN_TOKEN = "/login/token";

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post(LOGIN_TOKEN)
                .then().log().all().extract();
    }

    public static void 로그인_응답됨(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_됨(ExtractableResponse<Response> 로그인_요청_결과) {
        TokenResponse actual = 로그인_요청_결과.as(TokenResponse.class);
        assertThat(actual.getAccessToken()).isNotNull();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> 로그인_요청_실패_결과) {
        assertThat(로그인_요청_실패_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
