package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathApiHelper {
    public static ExtractableResponse<Response> 지하철_경로_조회_요청(Long 시작역, Long 종착역, String 토큰) {
        return RestAssured
            .given().log().all()
            .queryParam("source",시작역)
            .queryParam("target",종착역)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();
    }
}
