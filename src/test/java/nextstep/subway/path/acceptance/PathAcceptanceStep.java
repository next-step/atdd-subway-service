package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.apache.groovy.util.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static void 이용_요금_조회됨(PathResponse response, int expectedFare) {
        assertThat(response.getFare()).isNotNull();
        assertThat(response.getFare()).isEqualTo(expectedFare);
    }

    public static void expectBadRequest(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
