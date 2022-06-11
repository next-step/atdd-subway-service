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

    /**
     * Feature: 지하철 구간 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 구간을 관리
     *     When 지하철 구간 등록 요청
     *     Then 지하철 구간 등록됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 등록한 지하철 구간이 반영된 역 목록이 순서에 맞게 조회됨
     *     When 지하철 구간 삭제 요청
     *     Then 지하철 구간 삭제됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @DisplayName("지하철 구간을 관리한다.")
    @Test
    void 지하철_구간_관리_정상_시나리오() {
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        ExtractableResponse<Response> 신분당선_조회 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(신분당선_조회);
        지하철_노선에_지하철역_순서_정렬됨(신분당선_조회, Arrays.asList(정자역, 강남역, 양재역, 광교역));

        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        지하철_노선에_지하철역_제외됨(removeResponse);

        ExtractableResponse<Response> 삭제후_신분당선_조회 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(삭제후_신분당선_조회, Arrays.asList(정자역, 강남역, 광교역));
    }

    /**
     * Feature: 지하철 구간 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 구간을 관리
     *     When 지하철 구간에 이미 등록되어있는 구간을 등록 요청
     *     Then 지하철 구간 등록 실패함
     *     When 지하철 구간에 등록되지 않은 역으로 이루어진 구간을 등록 요청
     *     Then 지하철 구간 등록 실패함
     *     When 지하철 구간이 하나일 때 구간 삭제 요청
     *     Then 지하철 구간 삭제 실패함
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @DisplayName("지하철 구간을 관리 실패한다.")
    @Test
    void 지하철_구간_관리_비정상_시나리오() {
        ExtractableResponse<Response> 이미_등록된_구간_등록 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);
        지하철_노선에_지하철역_등록_실패됨(이미_등록된_구간_등록);

        ExtractableResponse<Response> 등록되지_않은_구간_등록 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);
        지하철_노선에_지하철역_등록_실패됨(등록되지_않은_구간_등록);

        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외_실패됨(removeResponse);

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
