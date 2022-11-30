package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.apache.groovy.util.Maps;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathAcceptanceStep {

    public static ExtractableResponse<Response> get(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().params(toParam(source, target)).get("/paths")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> toParam(StationResponse source, StationResponse target) {
        return Maps.of("source", source.getId() + "", "target", target.getId() + "");
    }
}
