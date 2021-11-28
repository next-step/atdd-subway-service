package nextstep.subway.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;

import static nextstep.subway.AcceptanceTest.*;

public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return post("/login/token", tokenRequest);
    }
}
