package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.http.MediaType;

public class AuthMemberApiHelper {

    public static ExtractableResponse<Response> 로그인을통한_토큰받기(String 이메일, String 비밀번호) {
        TokenRequest tokenRequest = new TokenRequest(이메일, 비밀번호);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 토큰을통해_내정보받기(String 토큰) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer "+토큰)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

}
