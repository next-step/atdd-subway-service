package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathAcceptanceStep {

    public static final String RESOURCES = "/paths";

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(Long startStationId, Long endStationId) {
        PathRequest pathRequest = new PathRequest(startStationId, endStationId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get(RESOURCES)
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단_경로_확인(PathResponse actual, PathResponse expected) {
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(expected.getDistance());
            assertThat(actual.getStations().size()).isEqualTo(expected.getStations().size());
            for (int i = 0; i < actual.getStations().size(); i++) {
                assertThat(actual.getStations().get(i)).isEqualTo(expected.getStations().get(i));
            }
        });
    }

    public static void 지하철_최단_경로_응답됨(ExtractableResponse<Response> 지하철_최단_경로_조회_요청_결과) {
        assertThat(지하철_최단_경로_조회_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단_경로_예외_응답됨(ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외) {
        assertThat(지하철_최단_경로_조회_요청_예외.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
