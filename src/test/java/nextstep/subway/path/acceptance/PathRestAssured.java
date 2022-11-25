package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class PathRestAssured {

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(Long upStationId, Long downStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", upStationId);
        params.put("target", downStationId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
