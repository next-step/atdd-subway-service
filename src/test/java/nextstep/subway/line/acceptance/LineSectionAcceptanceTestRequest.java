package nextstep.subway.line.acceptance;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_목록_응답됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_조회_요청;
import static nextstep.subway.request.AcceptanceTestRequest.delete;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSectionAcceptanceTestRequest {
    public static Executable 지하철_노선에_지하철역_제외_요청_및_확인(LineResponse line, StationResponse station) {
        return () -> {
            ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(line, station);

            지하철_노선에_지하철역_제외됨(removeResponse);
        };
    }

    public static Executable 지하철_노선에_지하철역_등록_요청_및_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록됨(response);
        };
    }

    public static Executable 지하철_노선에_지하철역_순서_정렬됨(LineResponse line, StationResponse ...stationResponses) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선_조회_요청(line);

            지하철_노선_목록_응답됨(response);

            지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(stationResponses));
        };
    }

    public static Executable 지하철_노선에_지하철역_등록_요멍_및_실패_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록_실패됨(response);
        };
    }

    public static Executable 지하철_노선에_지하철역_제외_요멍_및_실패_확인(LineResponse line, StationResponse stationResponse) {
        return () -> {
            ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(line, stationResponse);

            지하철_노선에_지하철역_제외_실패됨(removeResponse);
        };
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return AcceptanceTestRequest.post(
                Given.builder()
                        .contentType(ContentType.JSON)
                        .body(sectionRequest)
                        .build(),
                When.builder()
                        .uri("/lines/{lineId}/sections")
                        .pathParams(line.getId())
                        .build()
        );
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return delete(
                Given.builder().build(),
                When.builder()
                        .uri("/lines/{lineId}/sections?stationId={stationId}")
                        .pathParams(line.getId(), station.getId())
                        .build()
        );
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
