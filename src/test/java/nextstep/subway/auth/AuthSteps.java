package nextstep.subway.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

import static nextstep.subway.AcceptanceTest.*;

public class AuthSteps {

    public static String 로그인_되어_있음(String email, String password) {
        return 로그인_요청(email, password).getAccessToken();
    }

    public static TokenResponse 로그인_요청(String email, String password) {
        return 로그인_요청(new TokenRequest(email, password)).as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return post("/login/token", tokenRequest);
    }
}
