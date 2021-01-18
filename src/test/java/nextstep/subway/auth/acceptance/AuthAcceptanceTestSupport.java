package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTestSupport.회원_생성을_요청;

public class AuthAcceptanceTestSupport extends AcceptanceTest {

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_되어_있음() {
        회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        String accessToken = 로그인_요청(tokenRequest)
                .as(TokenResponse.class).getAccessToken();
        return accessToken;
    }

}
