package nextstep.subway.line.acceptance.step;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LineSectionAcceptanceStep {

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line,
        StationResponse upStation, StationResponse downStation, int distance) {
        지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line,
        StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(),
            distance);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(
        LineResponse line, List<StationResponse> expectedStations) {
        LineResponse lineResponse = 지하철_노선_조회_요청(line)
            .as(LineResponse.class);
        assertThat(lineResponse.getStations())
            .extracting(StationResponse::getId)
            .containsExactly(
                expectedStations.stream()
                    .map(StationResponse::getId)
                    .toArray(Long[]::new)
            );
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(Long lineId,
        Long stationId) {
        return RestAssured.given().log().all()
            .param("stationId", stationId)
            .when()
            .delete("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_역_못찾음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
