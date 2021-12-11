package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간 관리")
    @Test
    void handleLineSection() {
        // when 지하철 구간 사이에 역 추가
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then 지하철 노선에 등록된 역 확인
        지하철_노선_지하철역_등록_확인(신분당선, Arrays.asList(강남역, 양재역, 광교역));

        // when 지하철 구간 종점역 삭제
        ExtractableResponse<Response> 광교역삭제응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

        // then 지하철 노선 삭제 확인
        지하철_노선_지하철역_삭제_확인(광교역삭제응답, Arrays.asList(강남역, 양재역));

        // when 지하철 구간 종점에 역 추가
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 5);

        // then 지하철 노선에 등록된 역 확인
        지하철_노선_지하철역_등록_확인(신분당선, Arrays.asList(강남역, 양재역, 정자역));

        // when 지하철 구간 중간역 삭제
        ExtractableResponse<Response> 양재역삭제응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then 지하철 노선 삭제 확인
        지하철_노선_지하철역_삭제_확인(양재역삭제응답, Arrays.asList(강남역, 정자역));
    }

    private void 지하철_노선_지하철역_삭제_확인(ExtractableResponse<Response> deleteResponse, List<StationResponse> expected) {
        지하철_노선에_지하철역_제외됨(deleteResponse);
        ExtractableResponse<Response> 조회응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(조회응답, expected);
    }

    private void 지하철_노선_지하철역_등록_확인(LineResponse lineResponse, List<StationResponse> extected) {
        ExtractableResponse<Response> 양재역추가응답 = LineAcceptanceTest.지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_등록됨(양재역추가응답);
        지하철_노선에_지하철역_순서_정렬됨(양재역추가응답, extected);
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

}
