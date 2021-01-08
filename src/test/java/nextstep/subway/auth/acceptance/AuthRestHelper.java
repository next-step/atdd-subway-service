package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AuthRestHelper {

    public static String 토큰_구하기(String email, String password) {
        ExtractableResponse<Response> findResponse = 회원_로그인_요청(email, password);
        TokenResponse tokenResponse = findResponse.as(TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    public static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {

        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/login/token")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

}
