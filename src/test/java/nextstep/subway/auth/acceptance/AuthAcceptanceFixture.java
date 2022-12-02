package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthAcceptanceFixture {

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest 토큰_요청_정보) {
        return RestAssured.given().log().all()
                .body(토큰_요청_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_요청_성공(ExtractableResponse<Response> 로그인_요청_응답) {
        TokenResponse 응답_토큰 = 로그인_요청_응답.as(TokenResponse.class);
        assertAll(
                () -> assertThat(로그인_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(응답_토큰.getAccessToken()).isNotNull()
        );
    }

    public static void 로그인_요청_실패(ExtractableResponse<Response> 로그인_요청_응답) {
        assertThat(로그인_요청_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
