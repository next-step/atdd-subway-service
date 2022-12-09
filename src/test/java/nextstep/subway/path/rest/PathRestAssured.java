package nextstep.subway.path.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathRestAssured {

    public static ExtractableResponse<Response> 최단경로조회_요청하기(long sourceStationId, long targetStationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStationId)
                .param("target", targetStationId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
