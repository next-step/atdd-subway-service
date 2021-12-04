package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathFactory {

    public static ExtractableResponse<Response> 최단_경로를_조회(long source, long target, String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
