package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.springframework.http.MediaType;

public class MemberAcceptanceTestSupport extends AcceptanceTest {

    public static ExtractableResponse<Response> 나의_정보_요청(String token) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
