package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.MediaType;

public class AuthAcceptanceTestStep {

    public static final String LOGIN = "/login/token";
    public static final String MEMBERS_ME = "/members/me";

    static ExtractableResponse<Response> 내_정보_조회(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(MEMBERS_ME)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(memberRequest.getEmail(), memberRequest.getPassword()))
                .when()
                .post(LOGIN)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return 로그인_요청(new MemberRequest(email, password, null));
    }
}