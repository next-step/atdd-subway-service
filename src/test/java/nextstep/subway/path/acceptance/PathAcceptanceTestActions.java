package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceTestActions {

    public static ExtractableResponse<Response> 출발역과_도착역_입력(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static void 최단_경로가_조회됨(ExtractableResponse<Response> response, int distance,
                                  StationResponse... stationResponses) {
        assertAll(
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance),
                () -> assertThat(response.jsonPath().getList("stations.name", String.class))
                        .containsExactly(stationResponses[0].getName(), stationResponses[1].getName(),
                                stationResponses[2].getName()),
                () -> assertThat(response.jsonPath().getInt("cost")).isEqualTo(1350)
        );
    }

    public static void 조회_불가능(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
