package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.request.AcceptanceTestRequest.get;
import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTestRequest {
    public static Executable 지하철_최단거리_요청_및_실패(StationResponse source, StationResponse target) {
        return () -> {
            ExtractableResponse<Response> response = 최단거리_조회_요청(source, target);

            지하철_최단거리_실패됨(response);
        };
    }

    public static Executable 지하철_최단거리_요청_및_확인(StationResponse source, StationResponse target, List<StationResponse> exceptStations, int exceptDistance) {
        return () -> {
            ExtractableResponse<Response> response = 최단거리_조회_요청(source, target);

            지하철_최단거리_응답됨(response);

            지하철_최단거리_검증(response, exceptStations, exceptDistance);
        };
    }

    public static ExtractableResponse<Response> 최단거리_조회_요청(StationResponse source, StationResponse target) {
        return get(
                Given.builder()
                        .param("source", source.getId())
                        .param("target", target.getId())
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .build(),
                When.builder().uri("/paths").build()
        );
    }

    public static void 지하철_최단거리_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LinePathResponse.class)).isNotNull();
    }

    public static void 지하철_최단거리_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_최단거리_검증(ExtractableResponse<Response> response, List<StationResponse> exceptStations, int exceptDistance) {
        LinePathResponse pathResponse = response.as(LinePathResponse.class);

        List<Long> exceptStationIds = exceptStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(pathResponse.getStations())
                .map(StationResponse::getId)
                .containsExactlyInAnyOrderElementsOf(exceptStationIds);

        assertThat(pathResponse.getDistance())
                .isEqualTo(exceptDistance);
    }
}
