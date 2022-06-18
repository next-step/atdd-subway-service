package nextstep.subway.auth.factory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.MediaType;

public class AuthAcceptanceFactory {

    public static ExtractableResponse<Response> 로그인_요청(String name, String email) {
        TokenRequest tokenRequest = TokenRequest.of(name, email);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static TokenResponse 로그인_되어있음(String name, String email) {
        return 로그인_요청(name, email).as(TokenResponse.class);
    }
}
