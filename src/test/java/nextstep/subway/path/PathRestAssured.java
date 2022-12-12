package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathRestAssured {
    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/paths?source={sourceId}&target={targetId}", sourceStationId, targetStationId)
                .then().log().all().
                        extract();
    }
}
