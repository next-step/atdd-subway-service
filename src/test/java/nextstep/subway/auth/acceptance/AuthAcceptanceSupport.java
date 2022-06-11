package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceSupport {

    public static ExtractableResponse<Response> 로그인_시도함(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static void 로그인_성공됨(ExtractableResponse<Response> response) {
        String token = response.jsonPath().getString("accessToken");

        assertThat(token).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static String 로그인_성공후_토큰_조회됨(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    public static void 정보가_달라_로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
