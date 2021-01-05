package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
public class PathRestHelper {

    public static ExtractableResponse<Response> 지하철_경로_탐색_요청(long sourceId, long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(sourceId));
        params.put("target", String.valueOf(targetId));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
