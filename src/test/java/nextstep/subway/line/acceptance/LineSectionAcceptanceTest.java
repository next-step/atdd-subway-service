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

    @DisplayName("지하철 구간을 순서 없이 등록하고 조회한다.")
    @Test
    void addLineSection() {
        //when
        ExtractableResponse<Response> 강남역_양재역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        //then
        지하철_노선에_지하철역_등록됨(강남역_양재역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 양재역_정자역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3);
        //then
        지하철_노선에_지하철역_등록됨(양재역_정자역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        //then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답, 강남역, 양재역, 정자역, 광교역);
    }

    @DisplayName("지하철 구간을 제일 상행을 제외하고 등록하고 조회한다.")
    @Test
    void upStationRemove() {
        //when
        ExtractableResponse<Response> 강남역_양재역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        //then
        지하철_노선에_지하철역_등록됨(강남역_양재역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 강남역_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // then
        지하철_노선에_지하철역_제외됨(강남역_제외_요청_응답);

        //when
        ExtractableResponse<Response> 지하철_노선_조회_요청 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청, 양재역, 광교역);
    }

    @DisplayName("지하철 구간을 제일 히행을 제외하고 등록하고 조회한다.")
    @Test
    void downStationRemove() {
        //when
        ExtractableResponse<Response> 강남역_양재역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        //then
        지하철_노선에_지하철역_등록됨(강남역_양재역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 강남역_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);
        // then
        지하철_노선에_지하철역_제외됨(강남역_제외_요청_응답);

        //when
        ExtractableResponse<Response> 지하철_노선_조회_요청 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청, 강남역, 양재역);
    }

    @DisplayName("지하철 구간중 중간을 제외하고 등록하고 조회한다.")
    @Test
    void middleStationRemove() {
        //when
        ExtractableResponse<Response> 강남역_양재역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        //then
        지하철_노선에_지하철역_등록됨(강남역_양재역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 광교역_정자역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 정자역, 3);
        //then
        지하철_노선에_지하철역_등록됨(광교역_정자역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 강남역_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);
        // then
        지하철_노선에_지하철역_제외됨(강남역_제외_요청_응답);

        //when
        ExtractableResponse<Response> 지하철_노선_조회_요청 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청, 강남역, 양재역, 정자역);
    }

    @DisplayName("지하철 구간 등록 실패를 한다.")
    @Test
    void addLineFailed() {
        //when
        ExtractableResponse<Response> 강남역_양재역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);
        //then
        지하철_노선에_지하철역_등록_실패됨(강남역_양재역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 양재역_정자역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3);
        //then
        지하철_노선에_지하철역_등록_실패됨(양재역_정자역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 강남역_정자역_구간_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 100);
        //then
        지하철_노선에_지하철역_등록_실패됨(강남역_정자역_구간_등록_응답);

        //when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        //then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답, 강남역, 광교역);
    }

    @DisplayName("지하철 구간 제외 실패를 한다.")
    @Test
    void removeLineStationFailed() {
        // when
        ExtractableResponse<Response> 양재역_노선_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        // then
        지하철_노선에_지하철역_제외_실패됨(양재역_노선_제외_요청_응답);

        // when
        ExtractableResponse<Response> 강남역_노선_제외_요청_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // then
        지하철_노선에_지하철역_제외_실패됨(강남역_노선_제외_요청_응답);

        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답, 강남역, 광교역);
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

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, StationResponse ...expectedStations) {
        List<StationResponse> stationResponses = Arrays.asList(expectedStations);
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = stationResponses.stream()
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
