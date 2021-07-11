package nextstep.subway.line.domain;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.domain.LineTestSnippet.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSectorTestSnippet {


    public static Executable 지하철_노선에_지하철역_등록_요청_및_성공_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록됨(response);
        };
    }

    public static Executable 지하철_노선에_지하철역_등록_요청_및_실패_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록_실패됨(response);
        };
    }

    public static Executable 지하철_노선_조회_요청_및_확인(LineResponse response, List<StationResponse> 노선_내_정렬된_역) {
        return () -> {
            ExtractableResponse<Response> getResponse = 지하철_노선_조회_요청(response);

            지하철_노선_조회됨(getResponse);

            지하철_노선에_지하철역_순서_정렬됨(getResponse, 노선_내_정렬된_역);
        };
    }

    public static Executable 지하철_노선에_지하철역_제외_요청_및_성공_확인(LineResponse line, StationResponse station) {
        return () -> {
            ExtractableResponse<Response> deleteResponse = 지하철_노선에_지하철역_제외_요청(line, station);

            지하철_노선에_지하철역_제외됨(deleteResponse);
        };
    }

    public static Executable 지하철_노선에_지하철역_제외_요청_및_실패_확인(LineResponse line, StationResponse station) {
        return () -> {
            ExtractableResponse<Response> deleteResponse = 지하철_노선에_지하철역_제외_요청(line, station);

            지하철_노선에_지하철역_제외_실패됨(deleteResponse);
        };
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
