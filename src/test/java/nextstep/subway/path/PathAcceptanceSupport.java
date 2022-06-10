package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceSupport {
    public static ExtractableResponse<Response> 지하철역_최단경로_조회_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    public static void 지하철역_최단경로_검증_완료(ExtractableResponse<Response> response, int distance, String... stationNames) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly(stationNames);
    }

    public static void 지하철역_최단경로_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}