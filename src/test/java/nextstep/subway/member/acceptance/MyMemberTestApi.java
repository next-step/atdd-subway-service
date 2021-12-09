package nextstep.subway.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.MediaType;

public class MyMemberTestApi {
    private static final String MY_INFO_API_URL = "/members/me";

    public static ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(MY_INFO_API_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse tokenResponse, MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(memberRequest)
                .when().put(MY_INFO_API_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse tokenResponse) {
        return 내_정보_조회_요청(tokenResponse.getAccessToken());
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get(MY_INFO_API_URL)
                .then().log().all()
                .extract();
    }
}
